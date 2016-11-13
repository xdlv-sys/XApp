package xd.dl.action;

import com.alipay.api.internal.util.XmlUtils;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Element;
import xd.dl.AliPayClient;
import xd.dl.bean.DlOrder;
import xd.dl.service.DlService;
import xd.fw.FwUtil;
import xd.fw.HttpClientTpl;
import xd.fw.WxUtil;
import xd.fw.action.BaseAction;
import xd.fw.bean.wx.UnifiedOrder;
import xd.fw.scheduler.CloseOrderEvent;
import xd.fw.scheduler.RefundEvent;
import xd.fw.service.SetParameters;

@Results({
        @Result(name = "qr", type="dispatcher",location = "../../wwt/qr.jsp")
})
public class PayAction extends BaseAction {
    @Value("${app_id}")
    String appId;
    @Value("${mch_id}")
    String mchId;
    @Value("${wx_key}")
    String wxKey;
    @Value("${body}")
    String body;

    @Value("${wx_notify_url}")
    String wxNotifyUrl;

    @Value("${ali_notify_url}")
    String aliNotifyUrl;

    @Autowired
    DlService dlService;
    @Autowired
    AliPayClient aliPayClient;

    float money;
    int scale = 100;
    String userno;
    String donecode;

    String codeUrl;

    public String wxPay() throws Exception{
        UnifiedOrder unifiedOrder = new UnifiedOrder();
        unifiedOrder.setAppid(appId);
        unifiedOrder.setMch_id(mchId);
        unifiedOrder.setNonce_str(WxUtil.getRandomStringByLength(16));
        unifiedOrder.setBody(body);
        unifiedOrder.setOut_trade_no(donecode);
        unifiedOrder.setTotal_fee((int) (money * 100));
        unifiedOrder.setSpbill_create_ip(ServletActionContext.getRequest().getRemoteAddr());
        unifiedOrder.setNotify_url(wxNotifyUrl);

        unifiedOrder.setTrade_type("NATIVE");
        unifiedOrder.setProduct_id(donecode);
        unifiedOrder.setSign(WxUtil.getSign(unifiedOrder, wxKey));
        String xml = WxUtil.constructUnifiedOrderXml(unifiedOrder);

        log.info("unifiedOrder request xml:" + xml);
        String retXml = HttpClientTpl.postJson("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
        log.info("unifiedOrder response xml:" + retXml);

        Element rootEle = XmlUtils.getRootElementFromString(retXml);
        String returnCode = XmlUtils.getElementValue(rootEle, "return_code");
        String resultCode = XmlUtils.getElementValue(rootEle, "result_code");
        if (SUCCESS_FLAG.equals(returnCode) && SUCCESS_FLAG.equals(resultCode)) {
            codeUrl = XmlUtils.getElementValue(rootEle, "code_url");
            String tradeNo = XmlUtils.getElementValue(rootEle, "prepay_id");
            saveOrder(tradeNo, PAY_WX);
        } else {
            log.warn("failed to create wx order, please check the reason");
        }
        return SUCCESS;
    }

    private void saveOrder(String tradeNo, short payType) throws Exception{
        DlOrder dlOrder = new DlOrder(donecode,tradeNo,userno,money,payType,codeUrl);
        dlService.save(dlOrder);

        /*byte[] data = FwUtil.qrCode(qr, scale, scale);
        String base64Data = Base64.getEncoder().encodeToString(data);
        setRequestAttribute("data", base64Data);
        setRequestAttribute("outTradeNo", dlOrder.getOutTradeNo());*/
    }

    public String aliPay() throws Exception{
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(aliNotifyUrl);
        request.setBizContent(String.format("{" +
                "    \"out_trade_no\":\"%s\"," +
                "    \"total_amount\":%.2f," +
                "    \"subject\":\"%s\"," +
                "    \"timeout_express\":\"90m\"}",donecode, money,body));
        log.info("ali pay content:{}", request.getBizContent());
        AlipayTradePrecreateResponse response = aliPayClient.getAlipayClient().execute(request);
        if (response.isSuccess()){
            codeUrl = response.getQrCode();
            saveOrder(response.getOutTradeNo(),PAY_ALI);
        } else {
            log.warn("failed to create ali order, please check the reason");
        }
        return SUCCESS;
    }

    public String cancelOrder(){
        DlOrder order;
        if (StringUtils.isBlank(donecode) ||
                (order = dlService.get(DlOrder.class,donecode)) == null){
            return FINISH;
        }

        if (order.getPayStatus() == STATUS_DONE){
            RefundEvent event;
            if (order.getPayFlag() == PAY_WX){
                event = RefundEvent.wxRefund(order.getOutTradeNo(),
                        (float)order.getTotalFee(),appId,mchId,wxKey);
            } else {
                event = RefundEvent.aliRefund(order.getOutTradeNo(),
                        (float)order.getTotalFee(),aliPayClient.getAppId(), aliPayClient.getRsaKey());
            }
            context.publishEvent(event);
        } else {
            //close order
            CloseOrderEvent event;
            if (order.getPayFlag() == PAY_WX){
                event = CloseOrderEvent.wxClose(order.getOutTradeNo(),appId,mchId,wxKey);
            } else {
                event = CloseOrderEvent.aliClose(order.getOutTradeNo(),
                        aliPayClient.getAppId(), aliPayClient.getRsaKey());
            }
            context.publishEvent(event);
        }

        return FINISH;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public void setScale(int scale) {
        if (scale > 100){
            this.scale = scale;
        }
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public void setDonecode(String donecode) {
        this.donecode = donecode;
    }

    public String getCodeUrl() {
        return codeUrl;
    }
}
