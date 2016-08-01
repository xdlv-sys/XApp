package xd.fw.job;

import org.springframework.stereotype.Service;
import xd.fw.bean.JknEvent;

@Service
public class NotifyUserUpdatedJob extends EventJob {

    @Override
    protected byte process(JknEvent event) {
        //TODO notify user updated
        return EV_DONE;
    }

    @Override
    protected JknEvent processType() {
        return USER_UPGRADE;
    }
}
