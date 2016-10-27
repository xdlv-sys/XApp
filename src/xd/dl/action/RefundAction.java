package xd.dl.action;

import org.springframework.beans.factory.annotation.Autowired;
import xd.dl.DlConst;
import xd.dl.bean.PayOrder;
import xd.dl.scheduler.RefundEvent;
import xd.dl.service.PayService;

public class RefundAction extends ParkBaseAction implements DlConst {
    String outTradeNo;
    @Autowired
    PayService payService;

    public String refund() {
        PayOrder order = payService.get(PayOrder.class, outTradeNo);
        // pay successfully and fail to notify
        assert order != null &&
                order.getPayStatus() == STATUS_DONE && order.getNotifyStatus() == STATUS_FAIL;

        applicationContext.publishEvent(new RefundEvent(order));
        return SUCCESS;
    }
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }
}
