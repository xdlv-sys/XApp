package xd.dl.scheduler;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.XmlUtils;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import org.apache.http.Consts;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import xd.dl.DlConst;
import xd.dl.bean.ParkInfo;
import xd.dl.bean.PayOrder;
import xd.dl.scheduler.RefundEvent;
import xd.dl.scheduler.WxCerts;
import xd.dl.service.ParkService;
import xd.dl.service.PayService;
import xd.fw.WxUtil;

import xd.fw.service.IConst;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Async
public class RefundListener implements ApplicationListener<RefundEvent>, IConst, DlConst {

    Logger logger = LoggerFactory.getLogger(RefundListener.class);
    @Autowired
    ParkService parkService;
    @Autowired
    PayService payService;

    @Autowired
    WxCerts wxCerts;

    @Override
    public void onApplicationEvent(RefundEvent refundEvent) {
        PayOrder payOrder = (PayOrder) refundEvent.getSource();
        logger.info("start to refund {} in {}", payOrder.getOutTradeNo(), payOrder.getParkId());
        ParkInfo parkInfo = parkService.get(ParkInfo.class, payOrder.getParkId());

        boolean success = false;

        if (payOrder.getPayFlag() == PAY_WX) {
            String fee = String.valueOf((int) (payOrder.getTotalFee() * 100));

            List<String> paramList = new ArrayList<>();
            StringBuffer xml = new StringBuffer("<xml>");

            construct(paramList, xml, "appid", parkInfo.getAppId());
            construct(paramList, xml, "mch_id", parkInfo.getMchId());
            construct(paramList, xml, "nonce_str", WxUtil.getRandomStringByLength(32));
            construct(paramList, xml, "op_user_id", parkInfo.getMchId());
            construct(paramList, xml, "out_refund_no", "8" + payOrder.getOutTradeNo());
            construct(paramList, xml, "out_trade_no", payOrder.getOutTradeNo());
            construct(paramList, xml, "refund_fee", fee);
            construct(paramList, xml, "total_fee", fee);

            String sign = WxUtil.getSign(paramList, parkInfo.getWxKey());
            construct(paramList, xml, "sign", sign);
            xml.append("</xml>");

            try {
                logger.info("refund request xml:" + xml);
                String retXml = wxHttp(parkInfo.getParkId(), xml.toString());
                logger.info("refund response xml:" + retXml);
                Element rootEle = XmlUtils.getRootElementFromString(retXml);
                String returnCode = XmlUtils.getElementValue(rootEle, "return_code");
                String resultCode = XmlUtils.getElementValue(rootEle, "result_code");
                success = SUCCESS_FLAG.equals(returnCode) && SUCCESS_FLAG.equals(resultCode);
            } catch (Exception e) {
                logger.error("", e);
            }
        } else {
            //ali refund
            try {

                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do"
                        , parkInfo.getAliAppId(), parkInfo.getAliShaRsaKey(), "json", "GBK", null);
                AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
                request.setBizContent(String.format("{" +
                        "\"out_trade_no\":\"%s\"," +
                        "\"refund_amount\":%.2f" +
                        "}", payOrder.getOutTradeNo(), payOrder.getTotalFee()));
                AlipayTradeRefundResponse response = alipayClient.execute(request);
                success = response.isSuccess();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        logger.info("refund {} for {}", success, payOrder.getOutTradeNo());

        short status = success ? ORDER_STATUS_REFUND_DONE : ORDER_STATUS_REFUND_FAIL;
        payService.updatePayOrderStatus(payOrder.getOutTradeNo(), status);
    }

    void construct(List<String> paramList, StringBuffer xml, String key, String value) {
        paramList.add(key + "=" + value + "&");
        xml.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
    }

    String wxHttp(String parkId, String xml) throws IOException {
        try (CloseableHttpClient httpClient = wxCerts.getClientByParkId(parkId)) {
            HttpPost post = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");

            StringEntity jsonEntity = new StringEntity(xml, Consts.UTF_8);
            jsonEntity.setContentType("text/xml");
            post.setEntity(jsonEntity);
            return EntityUtils.toString(httpClient.execute(post).getEntity(), Consts.UTF_8);
        }
    }
}