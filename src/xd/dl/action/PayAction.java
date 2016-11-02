package xd.dl.action;


import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.XmlUtils;
import com.opensymphony.xwork2.Action;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Element;
import xd.dl.DlConst;
import xd.dl.bean.CarParkInfo;
import xd.dl.bean.ParkInfo;
import xd.dl.bean.PayOrder;
import xd.dl.mina.ParkHandler;
import xd.fw.AliPayUtil;
import xd.fw.FwException;
import xd.fw.HttpClientTpl;
import xd.fw.WxUtil;
import xd.fw.bean.AliPayBean;
import xd.fw.bean.wx.QueryOrder;
import xd.fw.bean.wx.UnifiedOrder;
import xd.fw.bean.wx.WxOrder;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class PayAction extends ParkBaseAction implements DlConst {

    @Autowired
    ParkHandler parkHandler;
    @Value("${pay_body}")
    String body;
    @Value("${wx_notify_url}")
    String wxNotifyUrl;

    @Value("${ali_notify_url}")
    String notifyUrl;
    @Value("${ali_return_url}")
    String returnUrl;
    @Value("${ali_show_url}")
    String showUrl;
    @Value("${ali_it_b_pay}")
    String aliPayTimeout;

    String code, openid, nsukey, state;

    CarParkInfo carParkInfo;
    String watchId, parkId;
    byte carType;
    WxOrder wxOrder;
    PayOrder payOrder;

    boolean queryWxOrder = false;

    AliPayBean aliPayBean;

    public String execute() throws Exception {
        HttpSession session = ServletActionContext.getRequest().getSession();
        String openIdKey = "openId";
        if (this.openid == null) {
            this.openid = (String) session.getAttribute(openIdKey);
        }

        String[] ps = state.split("-");
        parkId = ps[0];
        if (ps.length > 1) {
            watchId = ps[1];
        }

        ParkInfo parkInfo = parkService.get(ParkInfo.class, parkId);
        if (this.openid == null) {
            String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s" +
                            "&secret=%s&code=%s&grant_type=authorization_code"
                    , parkInfo.getAppId(), parkInfo.getSecret(), code);
            String retJson = HttpClientTpl.get(url);
            JSONObject json = JSONObject.fromObject(retJson);
            this.openid = (String) json.get("openid");
            if (this.openid == null) {
                throw new FwException("抱歉，网络出现问题，无法获取用户信息:openid，请重试。");
            }
            log.info("get openid:" + this.openid);
            session.setAttribute(openIdKey, this.openid);
        }

        setRetAttribute(openIdKey, this.openid);

        setRetAttribute("parkId", parkId);
        if (watchId != null) {
            setRetAttribute("watchId", watchId);
        }
        setRetAttribute("parkName", parkInfo.getParkName());
        return INDEX;
    }

    public String aliPay() throws Exception {
        assertCarParkInfoLegalForPay();
        ParkInfo parkInfo = parkService.get(ParkInfo.class, carParkInfo.getParkId());

        aliPayBean = new AliPayBean();
        aliPayBean.setOut_trade_no(createOutTradeNo());
        aliPayBean.setPartner(parkInfo.getPartnerId());
        aliPayBean.setSubject(parkInfo.getParkName());
        aliPayBean.setNotify_url(notifyUrl);
        aliPayBean.setReturn_url(returnUrl);
        aliPayBean.setTotal_fee(String.format("%.2f", carParkInfo.getPrice()));
        aliPayBean.setShow_url(showUrl);
        aliPayBean.setBody(body);
        aliPayBean.setIt_b_pay(aliPayTimeout);
        aliPayBean.setSeller_id(parkInfo.getPartnerId());

        aliPayBean.setSign_type(AlipayConstants.SIGN_TYPE_RSA);
        aliPayBean.setSign(AliPayUtil.getSign(aliPayBean, parkInfo.getAliShaRsaKey()));

        PayOrder payOrder = new PayOrder(aliPayBean.getOut_trade_no(), parkInfo.getParkId()
                , carParkInfo.getCarNumber(), carParkInfo.getPrice(), (short)STATUS_INI, PAY_ALI, watchId, carType);
        //save the pay order
        payService.save(payOrder);
        return Action.SUCCESS;
    }

    public String queryCarNumber() throws Exception {
        ParkInfo parkInfo = parkService.get(ParkInfo.class, carParkInfo.getParkId());
        carParkInfo = parkHandler.getCarParkInfo(carParkInfo.getCarNumber(), carParkInfo.getParkId(), watchId, carType);
        if (carParkInfo != null) {
            carParkInfo.setWxPay((wxBrowser()) && StringUtils.isNotBlank(parkInfo.getAppId()));
            carParkInfo.setAliPay(StringUtils.isNotBlank(parkInfo.getPartnerId()));
        }
        return Action.SUCCESS;
    }

    public String wxPay() throws Exception {
        assertCarParkInfoLegalForPay();

        ParkInfo parkInfo = parkService.get(ParkInfo.class, carParkInfo.getParkId());
        UnifiedOrder unifiedOrder = new UnifiedOrder();

        unifiedOrder.setAppid(parkInfo.getAppId());
        unifiedOrder.setMch_id(parkInfo.getMchId());
        unifiedOrder.setNonce_str(WxUtil.getRandomStringByLength(16));
        unifiedOrder.setBody(body);
        unifiedOrder.setOut_trade_no(createOutTradeNo());
        unifiedOrder.setTotal_fee((int) (carParkInfo.getPrice() * 100));
        unifiedOrder.setSpbill_create_ip(ServletActionContext.getRequest().getRemoteAddr());
        unifiedOrder.setNotify_url(wxNotifyUrl);
        unifiedOrder.setLimit_pay(parkInfo.getLimitPay());
        unifiedOrder.setOpenid(openid);
        if (!wxBrowser()){
            unifiedOrder.setTrade_type("APP");
            logger.info("open wx in another browser");
        }
        unifiedOrder.setSign(WxUtil.getSign(unifiedOrder, parkInfo.getWxKey()));

        String xml = WxUtil.constructUnifiedOrderXml(unifiedOrder);
        log.info("unifiedOrder request xml:" + xml);
        String retXml = HttpClientTpl.postJson("https://api.mch.weixin.qq.com/pay/unifiedorder", xml);
        log.info("unifiedOrder response xml:" + retXml);
        Element rootEle = XmlUtils.getRootElementFromString(retXml);
        String returnCode = XmlUtils.getElementValue(rootEle, "return_code");
        String resultCode = XmlUtils.getElementValue(rootEle, "result_code");
        String prePayId = XmlUtils.getElementValue(rootEle, "prepay_id");
        if (SUCCESS_FLAG.equals(returnCode) && SUCCESS_FLAG.equals(resultCode)) {
            //wxOrder is used to launch wx pay
            wxOrder = new WxOrder();
            wxOrder.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
            wxOrder.setNonceStr(WxUtil.getRandomStringByLength(32));
            wxOrder.setAppId(parkInfo.getAppId());
            wxOrder.setPrePayId(prePayId);
            List<String> params = new ArrayList<>();
            params.add("appId=" + wxOrder.getAppId() + "&");
            params.add("timeStamp=" + wxOrder.getTimeStamp() + "&");
            params.add("nonceStr=" + wxOrder.getNonceStr() + "&");
            params.add("package=" + wxOrder.getPackage() + "&");
            params.add("signType=" + wxOrder.getSignType() + "&");

            wxOrder.setPaySign(WxUtil.getSign(params, parkInfo.getWxKey()));

            payOrder = new PayOrder(unifiedOrder.getOut_trade_no(), parkInfo.getParkId()
                    , carParkInfo.getCarNumber(), carParkInfo.getPrice(), (short)STATUS_INI, PAY_WX, watchId, carType);
            //save the pay order
            payService.save(payOrder);

        } else {
            log.warn("failed to create order, please check the reason");
        }
        return Action.SUCCESS;
    }

    private void assertCarParkInfoLegalForPay() throws Exception {
        carParkInfo = parkHandler.getCarParkInfo(carParkInfo.getCarNumber()
                , carParkInfo.getParkId(), watchId, carType);
        if (carParkInfo == null || carParkInfo.getPrice() == 0) {
            throw new Exception("can not pay since price is zero or no car info");
        }
    }

    public String queryNotifyStatus() throws Exception{
        payOrder = payService.get(PayOrder.class, payOrder.getOutTradeNo());
        return Action.SUCCESS;
    }

    public String queryPayStatus() throws Exception {
        payOrder = payService.get(PayOrder.class, payOrder.getOutTradeNo());
        if (payOrder.getPayStatus() == STATUS_INI && queryWxOrder) {
            log.info("start to query wx order");
            ParkInfo parkInfo = parkService.get(ParkInfo.class, payOrder.getParkId());
            QueryOrder queryOrder = new QueryOrder();
            queryOrder.setAppid(parkInfo.getAppId());
            queryOrder.setMch_id(parkInfo.getMchId());
            queryOrder.setNonce_str(WxUtil.getRandomStringByLength(16));
            queryOrder.setOut_trade_no(payOrder.getOutTradeNo());
            queryOrder.setSign(WxUtil.getSign(queryOrder, parkInfo.getWxKey()));

            String xml = WxUtil.constructUnifiedOrderXml(queryOrder);
            log.info("query order xml:" + xml);
            String retXml = HttpClientTpl.postJson("https://api.mch.weixin.qq.com/pay/orderquery", xml);
            log.info("query order response xml:" + retXml);
            Element element = XmlUtils.getRootElementFromString(retXml);
            String return_code = XmlUtils.getElementValue(element, "return_code");
            String result_code = XmlUtils.getElementValue(element, "result_code");
            String trade_state = XmlUtils.getElementValue(element, "trade_state");

            if (SUCCESS_FLAG.equals(return_code) && SUCCESS_FLAG.equals(result_code) &&
                    SUCCESS_FLAG.equals(trade_state)) {
                payOrder.setPayStatus((short)STATUS_DONE);
                payOrder.setTradeNo(XmlUtils.getChildElement(element, "transaction_id").getTextContent());
            } else {
                payOrder.setPayStatus((short)STATUS_FAIL);
            }
            payService.saveOrUpdate(payOrder);
        }
        return Action.SUCCESS;
    }

    public void setQueryWxOrder(boolean queryWxOrder) {
        this.queryWxOrder = queryWxOrder;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setNsukey(String nsukey) {
        this.nsukey = nsukey;
    }

    public void setState(String state) {
        this.state = state;
    }

    public CarParkInfo getCarParkInfo() {
        return carParkInfo;
    }

    public void setCarParkInfo(CarParkInfo carParkInfo) {
        this.carParkInfo = carParkInfo;
    }

    public void setWxOrder(WxOrder wxOrder) {
        this.wxOrder = wxOrder;
    }

    public WxOrder getWxOrder() {
        return wxOrder;
    }

    public AliPayBean getAliPayBean() {
        return aliPayBean;
    }

    public PayOrder getPayOrder() {
        return payOrder;
    }

    public void setPayOrder(PayOrder payOrder) {
        this.payOrder = payOrder;
    }

    public void setWatchId(String watchId) {
        this.watchId = watchId;
    }

    public void setCarType(byte carType) {
        this.carType = carType;
    }
}
