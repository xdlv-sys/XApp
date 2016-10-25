package xd.fw.action;

import com.alipay.api.internal.util.XmlUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Element;
import xd.fw.FwUtil;
import xd.fw.HttpClientTpl;
import xd.fw.WxUtil;
import xd.fw.bean.DlOrder;
import xd.fw.bean.wx.UnifiedOrder;
import xd.fw.service.DlService;

import java.util.Base64;
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

    @Autowired
    DlService dlService;

    float money;
    int scale = 100;
    String userno;
    String donecode;

    public String wxPay() throws Exception {
        UnifiedOrder unifiedOrder = new UnifiedOrder();
        unifiedOrder.setAppid(appId);
        unifiedOrder.setMch_id(mchId);
        unifiedOrder.setNonce_str(WxUtil.getRandomStringByLength(16));
        unifiedOrder.setBody(body);
        unifiedOrder.setOut_trade_no(FwUtil.createOutTradeNo());
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
        String codeUrl = XmlUtils.getElementValue(rootEle, "code_url");
        if (SUCCESS_FLAG.equals(returnCode) && SUCCESS_FLAG.equals(resultCode)) {
            DlOrder dlOrder = new DlOrder(unifiedOrder.getOut_trade_no(),donecode,userno,money,PAY_WX);
            dlService.save(dlOrder);

            byte[] data = FwUtil.qrCode(codeUrl, scale, scale);
            String base64Data = Base64.getEncoder().encodeToString(data);
            setRequestAttribute("data", base64Data);
            setRequestAttribute("outTradeNo", dlOrder.getOutTradeNo());
        } else {
            log.warn("failed to create order, please check the reason");
        }
        return "qr";
    }
    public String aliPay() throws Exception{
        return SUCCESS;
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

}
