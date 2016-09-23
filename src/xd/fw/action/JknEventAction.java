package xd.fw.action;

import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.FwUtil;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;
import xd.fw.service.JknService;

import java.sql.Timestamp;
import java.util.List;

public class JknEventAction extends BaseAction {
    @Autowired
    JknService jknService;
    List<JknEvent> jknEvents;

    public String obtainEvents(){
        total = jknService.getAllCount(JknEvent.class);
        jknEvents = jknService.getList(JknEvent.class,null, start,limit);
        return SUCCESS;
    }

    public String triggerEvents(){
        FwUtil.safeEach(jknEvents, new FwUtil.SafeEachProcess<JknEvent>() {
            @Override
            public void process(JknEvent event) {
                JknEvent jknEvent = jknService.get(JknEvent.class, event.getEventId());
                jknEvent.setTryCount((byte)0);
                jknEvent.setEventStatus(ES_INI);
                jknService.update(jknEvent);
            }
        });
        return SUCCESS;
    }
    public String addEvents(){
        FwUtil.safeEach(jknEvents, new FwUtil.SafeEachProcess<JknEvent>() {
            @Override
            public void process(JknEvent event) {
                event.setTryCount((byte)0);
                event.setEventStatus(ES_INI);
                event.setTriggerDate(new Timestamp(System.currentTimeMillis()));
                jknService.save(event);
            }
        });
        return SUCCESS;
    }


    public void setJknEvents(List<JknEvent> jknEvents) {
        this.jknEvents = jknEvents;
    }

    public List<JknEvent> getJknEvents() {
        return jknEvents;
    }
}
