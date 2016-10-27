package xd.dl.scheduler;

import org.springframework.context.ApplicationEvent;
import xd.dl.bean.PayOrder;

public class RefundEvent extends ApplicationEvent {

    public RefundEvent(PayOrder source) {
        super(source);
    }
}
