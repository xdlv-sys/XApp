package xd.fw.scheduler;

import org.springframework.context.ApplicationEvent;
import xd.fw.bean.User;

public class NotifyTradeEvent extends ApplicationEvent {

    public NotifyTradeEvent(User source) {
        super(source);
    }
}
