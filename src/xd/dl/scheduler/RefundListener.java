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
<<<<<<< ad674155dd3266a10bf86d9535f55c7cf32768c3:src/xd/fw/scheduler/RefundListener.java
=======

>>>>>>> v1.0:src/xd/dl/scheduler/RefundListener.java
import xd.fw.service.IConst;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
<<<<<<< ad674155dd3266a10bf86d9535f55c7cf32768c3:src/xd/fw/scheduler/RefundListener.java
//@Async
public abstract class RefundListener implements ApplicationListener<RefundEvent>, IConst {
=======
@Async
public class RefundListener implements ApplicationListener<RefundEvent>, IConst, DlConst {

>>>>>>> v1.0:src/xd/dl/scheduler/RefundListener.java
    Logger logger = LoggerFactory.getLogger(RefundListener.class);

    @Autowired
    WxCerts wxCerts;

    @Override
<<<<<<< ad674155dd3266a10bf86d9535f55c7cf32768c3:src/xd/fw/scheduler/RefundListener.java
    public void onApplicationEvent(RefundEvent event){
        logger.info("start to refund {} in {}", event.getOutTradeNo(), event.getId());
        boolean success = false;

        if (event.getPayType() == PAY_WX) {
            String fee = String.valueOf((int)(event.getTotalFee() * 100));
=======
    public void onApplicationEvent(RefundEvent refundEvent) {
        PayOrder payOrder = (PayOrder) refundEvent.getSource();
        logger.info("start to refund {} in {}", payOrder.getOutTradeNo(), payOrder.getParkId());
        ParkInfo parkInfo = parkService.get(ParkInfo.class, payOrder.getParkId());

        boolean success = false;

        if (payOrder.getPayFlag() == PAY_WX) {
            String fee = String.valueOf((int) (payOrder.getTotalFee() * 100));

>>>>>>> v1.0:src/xd/dl/scheduler/RefundListener.java
            List<String> paramList = new ArrayList<>();
            StringBuffer xml = new StringBuffer("<xml>");

            construct(paramList, xml, "appid", event.getAppId());
            construct(paramList, xml, "mch_id", event.getMchId());
            construct(paramList, xml, "nonce_str", WxUtil.getRandomStringByLength(32));
            construct(paramList, xml, "op_user_id", event.getMchId());
            construct(paramList, xml, "out_refund_no", "8" + event.getOutTradeNo());
            construct(paramList, xml, "out_trade_no", event.getOutTradeNo());
            construct(paramList, xml, "refund_fee", fee);
            construct(paramList, xml, "total_fee", fee);

            String sign = WxUtil.getSign(paramList, event.getWxKey());
            construct(paramList, xml, "sign", sign);
            xml.append("</xml>");

            try {
                logger.info("refund request xml:" + xml);
                String retXml = wxHttp(event.getId(), xml.toString());
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
<<<<<<< ad674155dd3266a10bf86d9535f55c7cf32768c3:src/xd/fw/scheduler/RefundListener.java
                        ,event.getAppId(),event.getRsaKey(),"json","GBK",null);
=======
                        , parkInfo.getAliAppId(), parkInfo.getAliShaRsaKey(), "json", "GBK", null);
>>>>>>> v1.0:src/xd/dl/scheduler/RefundListener.java
                AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
                request.setBizContent(String.format("{" +
                        "\"out_trade_no\":\"%s\"," +
                        "\"refund_amount\":%.2f" +
                        "}", event.getOutTradeNo(), event.getTotalFee()));
                AlipayTradeRefundResponse response = alipayClient.execute(request);
                success = response.isSuccess();
            } catch (Exception e) {
                logger.error("", e);
            }
        }
<<<<<<< ad674155dd3266a10bf86d9535f55c7cf32768c3:src/xd/fw/scheduler/RefundListener.java
        logger.info("refund {} for {}", success , event.getOutTradeNo());

        processRefundStatus(event.getOutTradeNo(), success);
=======
        logger.info("refund {} for {}", success, payOrder.getOutTradeNo());

        short status = success ? ORDER_STATUS_REFUND_DONE : ORDER_STATUS_REFUND_FAIL;
        payService.updatePayOrderStatus(payOrder.getOutTradeNo(), status);
>>>>>>> v1.0:src/xd/dl/scheduler/RefundListener.java
    }

    protected abstract void processRefundStatus(String outTradeNo, boolean success);

    void construct(List<String> paramList, StringBuffer xml, String key, String value) {
        paramList.add(key + "=" + value + "&");
        xml.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
    }

<<<<<<< ad674155dd3266a10bf86d9535f55c7cf32768c3:src/xd/fw/scheduler/RefundListener.java
    String wxHttp(String id, String xml) throws IOException {
        try(CloseableHttpClient httpClient = wxCerts.getClientById(id)){
=======
    String wxHttp(String parkId, String xml) throws IOException {
        try (CloseableHttpClient httpClient = wxCerts.getClientByParkId(parkId)) {
>>>>>>> v1.0:src/xd/dl/scheduler/RefundListener.java
            HttpPost post = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");

            StringEntity jsonEntity = new StringEntity(xml, Consts.UTF_8);
            jsonEntity.setContentType("text/xml");
            post.setEntity(jsonEntity);
            return EntityUtils.toString(httpClient.execute(post).getEntity(), Consts.UTF_8);
        }
    }
}