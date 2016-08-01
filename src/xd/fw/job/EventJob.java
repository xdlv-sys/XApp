package xd.fw.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.bean.JknEvent;
import xd.fw.service.JknService;

import java.util.List;

@Service
public abstract class EventJob extends BaseJob {
    public static JknEvent USER_UPDATE = new JknEvent(EV_USER_UPDATE, EV_INI),
            ADD_ORDER = new JknEvent(EV_ADD_ORDER, EV_INI),
            USER_UPGRADE = new JknEvent(EV_USER_UPGRADE,EV_INI);

    @Value("${batch_event}")
    int batchEvent = 100;

    @Autowired
    JknService jknService;

    @Override
    public final void doExecute() throws Exception {
        List<JknEvent> events = getEvent(processType());
        if (events == null || events.size() < 1){
            return;
        }
        logger.info("start to process event:" + processType().getEventType());
        byte eventStatus;
        for (JknEvent event : events){
            eventStatus = process(event);
            event.setEventStatus(eventStatus);
            jknService.update(event);
        }
        logger.info("end to process event:" + processType().getEventType());
    }
    protected abstract byte process(JknEvent event);

    protected abstract JknEvent processType();

    protected List<JknEvent> getEvent(JknEvent type) {
        return jknService.getList(JknEvent.class, type, null, 0, 100);
    }
}
