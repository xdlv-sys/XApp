package xd.fw.action;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import xd.fw.bean.Const;
import xd.fw.bean.PayOrder;
import xd.fw.scheduler.RefundEvent;
import xd.fw.service.PayService;

public class RefundAction extends ParkBaseAction implements Const {
    String outTradeNo;
    @Autowired
    PayService payService;

    public String refund() {
        PayOrder order = payService.get(PayOrder.class, outTradeNo);
        // pay successfully and fail to notify
        assert order != null &&
                order.getPayStatus() == STATUS_SUCCESS && order.getNotifyStatus() == STATUS_FAIL;

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
