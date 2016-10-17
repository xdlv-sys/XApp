package xd.fw.scheduler;

import org.springframework.context.ApplicationEvent;
import xd.fw.bean.PayOrder;

public class RefundEvent extends ApplicationEvent {

    public RefundEvent(PayOrder source) {
        super(source);
    }
}
