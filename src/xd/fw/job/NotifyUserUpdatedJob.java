package xd.fw.job;

import org.springframework.stereotype.Service;
import xd.fw.bean.JknEvent;

@Service
public class NotifyUserUpdatedJob extends EventJob {

    @Override
    protected Byte process(JknEvent eventType) {
        //TODO notify user updated
        return EV_DONE;
    }

    @Override
    protected Byte[] processType() {
        return EV_USER_UPGRADE;
    }
}
