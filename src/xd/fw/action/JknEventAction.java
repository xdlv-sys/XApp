package xd.fw.action;

import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknUser;
import xd.fw.service.JknService;

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

    public void setJknEvents(List<JknEvent> jknEvents) {
        this.jknEvents = jknEvents;
    }

    public List<JknEvent> getJknEvents() {
        return jknEvents;
    }
}
