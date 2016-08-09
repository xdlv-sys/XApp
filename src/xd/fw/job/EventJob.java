package xd.fw.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.fw.JKN;
import xd.fw.bean.JknEvent;
import xd.fw.service.JknService;

import java.util.Arrays;
import java.util.List;

@Service
public abstract class EventJob extends BaseJob {

    @Autowired
    JknService jknService;

    @Override
    public final void doExecute() throws Exception {
        List<JknEvent> events = getEvent();
        if (events == null || events.size() < 1){
            return;
        }
        logger.info("start to process event:" + Arrays.toString(processType()));
        byte eventStatus = ES_FAIL;
        boolean serious = false;
        for (JknEvent event : events){
            try{
                eventStatus = process(event);
            } catch (Throwable e){
                logger.error("",e);
                serious = true;
            }
            if (eventStatus == ES_FAIL){
                jknService.triggerSmsEvent(event.getEventType(),serious);
            }
            event.setEventStatus(eventStatus);
            event.setTryCount((byte)(event.getTryCount() + 1));
            jknService.update(event);
        }
        logger.info("end to process event:" + Arrays.toString(processType()));
    }
    protected abstract byte process(JknEvent event) throws Exception;

    protected abstract byte[] processType();

    protected List<JknEvent> getEvent() {
        return jknService.getTriggeringEvent(processType(), 0, maxLimit());
    }

    protected int maxLimit(){
        return JKN.batch_event;
    }
}
