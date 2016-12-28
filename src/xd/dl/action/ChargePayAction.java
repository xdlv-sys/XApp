package xd.dl.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Value;
import xd.dl.bean.Charge;
import xd.dl.bean.ParkInfo;
import xd.fw.FwException;
import xd.fw.WxUtil;
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

    String code, state;
    Charge charge;

    @Action("chargePay")
    public String execute() throws Exception {
        String[] ps = state.split("-");
        String parkId = ps[0];
        float money = Float.parseFloat(ps[1]);

        ParkInfo parkInfo = parkService.get(ParkInfo.class, parkId);
        String openId = WxUtil.getOpenId(parkInfo.getAppId(), parkInfo.getSecret(), code);
        if (openId == null) {
            logger.error("can not obtain openId");
            throw new FwException("抱歉，网络出现问题，无法获取用户信息:openId，请重试。");
        }
        log.info("get openId:" + openId);

        // unified order
        String outTradeNo = createOutTradeNo();
        WxOrder wxOrder = WxUtil.unifiedOrder(parkInfo.getAppId(), parkInfo.getMchId(),parkInfo.getWxKey()
                ,openId,parkInfo.getParkName(),outTradeNo,money,wxNotifyUrl,parkInfo.getLimitPay());
        // save charge order
        Charge charge = new Charge(outTradeNo,parkId,money,(short) STATUS_INI,PAY_WX);
        payService.save(charge);

        setRetAttribute("wxOrder", wxOrder);
        setRetAttribute("charge", charge);
        return INDEX;
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
    /*public String aliPay() throws Exception {
        *//*assertCarParkInfoLegalForPay();
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
                , carParkInfo.getCarNumber(), carParkInfo.getPrice()
                , (short) STATUS_INI, PAY_ALI, watchId, carType
                , carParkInfo.getDbId(),carParkInfo.getStartTime());*//*
        //save the pay order
        payService.save(payOrder);
        return Action.SUCCESS;
    }*/


}
