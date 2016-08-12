package xd.fw.job;

import org.springframework.stereotype.Service;
import xd.fw.bean.JknEvent;

@Service
public class NotifyUserCountUpdateJob extends EventJob {

    @Override
    protected byte process(JknEvent event) {
        //TODO notify user updated
        return ES_DONE;
    }

    @Override
    protected byte[] processType() {
        return new byte[]{EV_USER_NOTIFY_COUNT};
    }


    @Override
    protected int maxLimit() {
        return 2000;
    }
}
