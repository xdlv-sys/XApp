package xd.dl.action;

import net.sf.json.JSONObject;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import xd.dl.bean.ParkInfo;
import xd.dl.bean.PayOrder;
import xd.dl.service.PayService;
import xd.fw.action.PayNotifyBaseAction;

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
    @Autowired
    PayService payService;
    @Override
    protected String wxKey(String out_trade_no) {
        payOrder = payService.get(PayOrder.class, out_trade_no);
        parkInfo = payService.get(ParkInfo.class, payOrder.getParkId());
        return parkInfo.getWxKey();
    }

    @Override
    protected boolean processOrder(String out_trade_no, String transaction_id, boolean success) {
        if (payOrder == null){
            return true;
        }
        if (payOrder.getPayStatus() != STATUS_INI){
            return true;
        }
        if (payOrder.getTotalFee() != totalFee()){
            return false;
        }
        payService.updateInitialPayOrderStatus(out_trade_no,transaction_id
                , success ? STATUS_DONE : STATUS_FAIL);
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
