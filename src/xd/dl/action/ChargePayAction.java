package xd.dl.action;

import com.alipay.api.AlipayConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Value;
import xd.dl.bean.Charge;
import xd.dl.bean.ParkInfo;
import xd.dl.bean.PayOrder;
import xd.fw.AliPayUtil;
import xd.fw.FwException;
import xd.fw.WxUtil;
import xd.fw.bean.AliPayBean;
import xd.fw.bean.wx.WxOrder;

/**
 * Created by xd on 12/26/2016.
 */
@Results({
        @Result(name = "index", location = "../../wwt/charge-pay.jsp")
})
public class ChargePayAction extends ParkBaseAction {
    @Value("${charge_wx_notify_url}")
    String wxNotifyUrl;

    @Value("${charge_ali_notify_url}")
    String notifyUrl;
    @Value("${charge_ali_return_url}")
    String returnUrl;
    @Value("${ali_show_url}")
    String showUrl;
    @Value("${ali_it_b_pay}")
    String aliPayTimeout;

    @Value("${charge_redirect_url}")
    String redirectUrl;
    @Value("${charge_body}")
    String chargeBody;

    String code, state;
    Charge charge;

    String wxUrl;
    AliPayBean aliPayBean;

    public String saveChargePay() throws Exception {
        charge.setOutTradeNo(createOutTradeNo());
        ParkInfo parkInfo = parkService.get(ParkInfo.class, charge.getParkId());

        if (wxBrowser()) {
            charge.setPayFlag(PAY_WX);
            wxUrl = String.format("https://open.weixin.qq.com/connect/oauth2" +
                            "/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base" +
                            "&state=%s#wechat_redirect", parkInfo.getAppId(), redirectUrl
                    , charge.getOutTradeNo());
        } else {
            charge.setPayFlag(PAY_ALI);
        }
        payService.save(charge);
        return SUCCESS;
    }

    @Action("chargeWxPay")
    public String chargeWxPay() throws Exception {
        charge = payService.get(Charge.class, state);

        ParkInfo parkInfo = parkService.get(ParkInfo.class, charge.getParkId());
        String openId = WxUtil.getOpenId(parkInfo.getAppId(), parkInfo.getSecret(), code);
        if (openId == null) {
            logger.error("can not obtain openId");
            throw new FwException("抱歉，网络出现问题，无法获取用户信息:openId，请重试。");
        }
        log.info("get openId:" + openId);

        // unified order
        WxOrder wxOrder = WxUtil.unifiedOrder(parkInfo.getAppId(), parkInfo.getMchId(), parkInfo.getWxKey()
                , openId, parkInfo.getParkName(), charge.getOutTradeNo()
                , charge.getTotalFee(), wxNotifyUrl, parkInfo.getLimitPay());
        setRetAttribute("wxOrder", wxOrder);
        setRetAttribute("charge", charge);
        return INDEX;
    }

    public String aliPay() throws Exception {
        charge = payService.get(Charge.class, charge.getOutTradeNo());
        ParkInfo parkInfo = parkService.get(ParkInfo.class, charge.getParkId());

        aliPayBean = new AliPayBean();
        aliPayBean.setOut_trade_no(createOutTradeNo());
        aliPayBean.setPartner(parkInfo.getPartnerId());
        aliPayBean.setSubject(parkInfo.getParkName());
        aliPayBean.setNotify_url(notifyUrl);
        aliPayBean.setReturn_url(returnUrl);
        aliPayBean.setTotal_fee(String.format("%.2f", charge.getTotalFee()));
        aliPayBean.setShow_url(showUrl);
        aliPayBean.setBody(chargeBody);
        aliPayBean.setIt_b_pay(aliPayTimeout);
        aliPayBean.setSeller_id(parkInfo.getPartnerId());

        aliPayBean.setSign_type(AlipayConstants.SIGN_TYPE_RSA);
        aliPayBean.setSign(AliPayUtil.getSign(aliPayBean, parkInfo.getAliShaRsaKey()));

        return SUCCESS;
    }

    public String queryPayStatus() throws Exception {
        charge = payService.get(Charge.class, charge.getOutTradeNo());
        return SUCCESS;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

    public Charge getCharge() {
        return charge;
    }

    public String getWxUrl() {
        return wxUrl;
    }

    public AliPayBean getAliPayBean() {
        return aliPayBean;
    }
}
