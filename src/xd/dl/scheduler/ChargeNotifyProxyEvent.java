package xd.dl.scheduler;

import org.springframework.context.ApplicationEvent;


public class ChargeNotifyProxyEvent extends ApplicationEvent {
    public ChargeNotifyProxyEvent(String source) {
        super(source);
    }

    public String getOutTradeNo(){
        return (String)getSource();
    }
}
