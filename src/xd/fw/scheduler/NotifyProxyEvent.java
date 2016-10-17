package xd.fw.scheduler;

import org.springframework.context.ApplicationEvent;


public class NotifyProxyEvent extends ApplicationEvent {
    public NotifyProxyEvent(String source) {
        super(source);
    }

    public String getOutTradeNo(){
        return (String)getSource();
    }
}
