package xd.dl.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xd.dl.bean.PayOrder;
import xd.dl.mina.ParkHandler;
import xd.dl.service.PayService;
import xd.fw.service.IConst;

import java.util.Date;

@Service
@Async
public class NotifyProxyListener implements ApplicationListener<NotifyProxyEvent>, IConst {

    Logger logger = LoggerFactory.getLogger(NotifyProxyListener.class);
    @Autowired
    PayService payService;

    @Autowired
    ParkHandler parkHandler;
    @Autowired
    TaskScheduler scheduler;

    @Value("${delay_notify_proxy}")
    long delayForNotifyProxy;

    @Override
    public void onApplicationEvent(NotifyProxyEvent notifyProxy) {
        PayOrder payOrder = payService.get(PayOrder.class, notifyProxy.getOutTradeNo());
        execute(payOrder, true);
    }

    private void execute(PayOrder payOrder, boolean first){
        logger.info("start to notify proxy {} first {}", payOrder.getOutTradeNo(),first);

        boolean success = parkHandler.payParkingFee(payOrder);
        if (!success){
            logger.warn("notify failed for {}" , payOrder.getOutTradeNo());
            if (first){
                logger.warn("try 10 seconds later {}" , payOrder.getOutTradeNo());
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        execute(payOrder, false);
                    }
                }, new Date(System.currentTimeMillis() + delayForNotifyProxy));
                return;
            }
            payOrder.setNotifyStatus((short)STATUS_FAIL);
        } else {
            payOrder.setNotifyStatus((short)STATUS_DONE);
        }
        logger.info("notify proxy result:{} for {}", payOrder.getNotifyStatus(), payOrder.getOutTradeNo());
        payService.updateNotifyStatus(payOrder.getOutTradeNo(), payOrder.getNotifyStatus());
    }
}
