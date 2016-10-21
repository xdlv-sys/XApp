package xd.fw.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.ReversedHandler;

@Service
@Async
public class UpgradeProxyListener implements ApplicationListener<UpgradeProxyEvent> {
    static Logger logger = LoggerFactory.getLogger(UpgradeProxyListener.class);
    @Autowired
    ReversedHandler reversedHandler;

    @Override
    public void onApplicationEvent(UpgradeProxyEvent upgradeProxyEvent){
        int[] source = (int[])upgradeProxyEvent.getSource();
        try {
            logger.info("start to upgrade {} from {}", source[0], source[1]);
            reversedHandler.upgrade(source[0], source[1]);
            logger.info("end to upgrade {} from {}", source[0], source[1]);
        } catch (Exception e) {
            logger.error("",e);
        }
    }
}
