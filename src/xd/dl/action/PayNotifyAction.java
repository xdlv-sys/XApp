package xd.dl.action;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate4.HibernateTemplate;
import xd.dl.bean.Charge;
import xd.dl.bean.ParkInfo;
import xd.dl.bean.PayOrder;
import xd.dl.scheduler.NotifyProxyEvent;
import xd.dl.service.PayService;
import xd.fw.action.PayNotifyBaseAction;
import xd.fw.service.SessionCommit;

/**
 * Created by xd on 10/27/2016.
 */
@Results({
        @Result(name = "pay", location = "../../wwt/pay.jsp")
})
public class PayNotifyAction extends PayNotifyBaseAction {
    final String RET_KEY = "RET_FOR_TOUCH" , PAY = "pay";

    PayOrder payOrder;
    ParkInfo parkInfo;

    Charge charge;
    @Autowired
    PayService payService;
    @Autowired
    ApplicationContext applicationContext;

    @Override
    protected String wxKey(String out_trade_no) {
        payOrder = payService.get(PayOrder.class, out_trade_no);
        String parkId;
        if (payOrder == null){
            charge = payService.get(Charge.class, out_trade_no);
            parkId = charge.getParkId();
        } else {
            parkId = payOrder.getParkId();
        }
        parkInfo = payService.get(ParkInfo.class, parkId);
        return parkInfo.getWxKey();
    }

    @Override
    protected boolean processOrder(String out_trade_no, String transaction_id, boolean success) {
        if (payOrder == null){
            return true;
        }
        //just return if the status of order was handled before
        if (payOrder.getPayStatus() != STATUS_INI){
            return true;
        }
        if (payOrder.getTotalFee() != totalFee()){
            return false;
        }
        payService.runSessionCommit(new SessionCommit() {
            @Override
            public void process(HibernateTemplate htpl) {
                PayOrder order = htpl.load(PayOrder.class, out_trade_no);
                short payStatus = success ? STATUS_DONE : STATUS_FAIL;
                order.setPayStatus(payStatus);
                if (StringUtils.isNotBlank(transaction_id)){
                    order.setTradeNo(transaction_id);
                }
                htpl.update(order);
            }
        });
        if (success){
            //insert notify task if the status is success
            applicationContext.publishEvent(new NotifyProxyEvent(out_trade_no));
        }
        return true;
    }

    @Override
    protected String pid(String out_trade_no) {
        payOrder = payService.get(PayOrder.class, out_trade_no);
        parkInfo = payService.get(ParkInfo.class, payOrder.getParkId());
        return parkInfo.getPartnerId();
    }

    @Override
    protected String aliReturnHook(String out_trade_no){
        setRequestAttribute(RET_KEY, JSONObject.fromObject(payOrder).toString());
        return PAY;
    }

    @Override
    protected String aliPublicKey() {
        return parkInfo.getAliPublicKey();
    }
}
