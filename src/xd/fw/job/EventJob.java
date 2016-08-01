package xd.fw.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xd.fw.bean.JknEvent;
import xd.fw.service.JknService;

import java.util.List;

@Service
public class EventJob extends BaseJob {
    public static JknEvent USER_UPDATE = new JknEvent(JknService.EV_USER_UPDATE, JknService.EV_INI),
            ADD_ORDER = new JknEvent(JknService.EV_ADD_ORDER, JknService.EV_INI);

    @Value("${batch_event}")
    int batchEvent = 100;

    @Autowired
    JknService jknService;

    @Override
    public void doExecute() throws Exception {

    }

    protected List<JknEvent> getEvent(JknEvent type) {
        return jknService.getList(JknEvent.class, type, null, 0, 100);
    }
}
