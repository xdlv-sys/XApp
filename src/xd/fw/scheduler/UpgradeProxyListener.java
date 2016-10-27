package xd.fw.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xd.fw.mina.tlv.ReversedHandler;

@Service
@Async
public class UpgradeProxyListener implements ApplicationListener<UpgradeProxyEvent> {
    static Logger logger = LoggerFactory.getLogger(UpgradeProxyListener.class);
    @Qualifier("parkHandler")
    @Autowired
    ReversedHandler reversedHandler;

    @Override
    public void onApplicationEvent(UpgradeProxyEvent upgradeProxyEvent){
        UpgradeProxyEvent.Upgrade source = (UpgradeProxyEvent.Upgrade)upgradeProxyEvent.getSource();
        try {
            logger.info("start to upgrade {} from {}", source.id, source.version);
            reversedHandler.upgrade(source.id, source.version);
            logger.info("end to upgrade {} from {}", source.id, source.version);
        } catch (Exception e) {
            logger.error("",e);
        }
    }
}
