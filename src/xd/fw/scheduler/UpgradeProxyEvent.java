package xd.fw.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import xd.fw.bean.ParkInfo;
import xd.fw.mina.ParkHandler;

public class UpgradeProxyEvent extends ApplicationEvent {

    public UpgradeProxyEvent(ParkInfo source) {
        super(source);
    }
}
