package xd.dl.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xd.dl.bean.Charge;
import xd.dl.bean.PayOrder;
import xd.dl.mina.ParkHandler;
import xd.dl.service.PayService;
import xd.fw.service.IConst;

import java.util.Date;

@Service
@Async
public class ChargeNotifyProxyListener implements ApplicationListener<ChargeNotifyProxyEvent>, IConst {

    Logger logger = LoggerFactory.getLogger(ChargeNotifyProxyListener.class);
    @Autowired
    PayService payService;

    @Autowired
    ParkHandler parkHandler;
    @Autowired
    TaskScheduler scheduler;

    @Value("${delay_notify_proxy}")
    long delayForNotifyProxy;

    @Override
    public void onApplicationEvent(ChargeNotifyProxyEvent notifyProxy) {
        Charge charge = payService.get(Charge.class, notifyProxy.getOutTradeNo());
        execute(charge, true);
    }

    private void execute(Charge charge, boolean first){
        logger.info("start to notify charge proxy {} first {}", charge.getOutTradeNo(),first);

        boolean success = parkHandler.chargeNotify(charge);
        if (!success){
            logger.warn("notify charge failed for {}" , charge.getOutTradeNo());
            if (first){
                logger.warn("try 10 seconds later {}" , charge.getOutTradeNo());
                scheduler.schedule(() -> execute(charge, false), new Date(System.currentTimeMillis() + delayForNotifyProxy));
                return;
            }
            charge.setNotifyStatus((short)STATUS_FAIL);
        } else {
            charge.setNotifyStatus((short)STATUS_DONE);
        }
        payService.update(charge);

        logger.info("notify charge proxy result:{} for {}", charge.getNotifyStatus(), charge.getOutTradeNo());
    }
}
