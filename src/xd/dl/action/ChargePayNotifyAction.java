package xd.dl.action;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate4.HibernateTemplate;
import xd.dl.bean.Charge;
import xd.dl.bean.ParkInfo;
import xd.dl.scheduler.ChargeNotifyProxyEvent;
import xd.dl.service.PayService;
import xd.fw.action.PayNotifyBaseAction;
import xd.fw.service.SessionCommit;

/**
 * Created by xd on 10/27/2016.
 */
@Results({
        @Result(name = "pay", location = "../../wwt/charge-pay.jsp")
})
public class ChargePayNotifyAction extends PayNotifyBaseAction {
    final String RET_KEY = "RET_FOR_TOUCH" , PAY = "pay";

    Charge charge;
    ParkInfo parkInfo;
    @Autowired
    PayService payService;
    @Autowired
    ApplicationContext applicationContext;

    @Action("chargeWxNotify")
    public String wxNotify() throws Exception {
        return super.wxNotify();
    }
    @Action("chargeAliNotify")
    public String aliNotify() throws Exception {
        return super.aliNotify();
    }
    @Action("chargeAliReturn")
    public String aliReturn() throws Exception {
        return super.aliReturn();
    }

    @Override
    protected String wxKey(String out_trade_no) {
        charge = payService.get(Charge.class, out_trade_no);
        parkInfo = payService.get(ParkInfo.class, charge.getParkId());
        return parkInfo.getWxKey();
    }

    @Override
    protected boolean processOrder(String out_trade_no, String transaction_id, boolean success) {
        if (charge == null){
            return true;
        }
        //just return if the status of order was handled before
        if (charge.getPayStatus() != STATUS_INI){
            return true;
        }
        if (charge.getTotalFee() != totalFee()){
            return false;
        }
        payService.runSessionCommit(new SessionCommit() {
            @Override
            public void process(HibernateTemplate htpl) {
                Charge charge = htpl.load(Charge.class, out_trade_no);
                short payStatus = success ? STATUS_DONE : STATUS_FAIL;
                charge.setPayStatus(payStatus);
                if (StringUtils.isNotBlank(transaction_id)){
                    charge.setTradeNo(transaction_id);
                }
                htpl.update(charge);
            }
        });
        if (success){
            //insert notify task if the status is success
            applicationContext.publishEvent(new ChargeNotifyProxyEvent(out_trade_no));
        }
        return true;
    }

    @Override
    protected String pid(String out_trade_no) {
        charge = payService.get(Charge.class, out_trade_no);
        parkInfo = payService.get(ParkInfo.class, charge.getParkId());
        return parkInfo.getPartnerId();
    }

    @Override
    protected String aliReturnHook(String out_trade_no){
        setRequestAttribute(RET_KEY, JSONObject.fromObject(charge).toString());
        return PAY;
    }

    @Override
    protected String aliPublicKey() {
        return parkInfo.getAliPublicKey();
    }
}
