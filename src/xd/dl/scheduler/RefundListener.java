package xd.dl.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xd.dl.bean.PayOrder;
import xd.dl.service.PayService;

@Service
@Async
public class RefundListener extends xd.fw.scheduler.RefundListener {
    @Autowired
    PayService payService;
    @Override
    protected void processRefundStatus(String outTradeNo, boolean success) {
        short status = success ? ORDER_STATUS_REFUND_DONE : ORDER_STATUS_REFUND_FAIL;
        payService.runSessionCommit(htpl -> {
            PayOrder order = htpl.load(PayOrder.class, outTradeNo);
            order.setPayStatus(status);
            htpl.update(order);
        });
    }
}