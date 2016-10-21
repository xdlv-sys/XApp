package xd.fw.scheduler;

import org.springframework.context.ApplicationEvent;

public class UpgradeProxyEvent extends ApplicationEvent {

    public UpgradeProxyEvent(int[] source) {
        super(source);
    }
}
