package xd.fw.scheduler;

import org.springframework.context.ApplicationEvent;

public class UpgradeProxyEvent extends ApplicationEvent {

    public UpgradeProxyEvent(Upgrade source) {
        super(source);
    }

    public static class Upgrade{
        public String id;
        public int version;
        public Upgrade(String id, int version){
            this.id = id;
            this.version = version;
        }
    }
}
