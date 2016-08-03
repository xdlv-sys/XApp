package xd.fw.job;

import org.springframework.stereotype.Service;
import xd.fw.bean.JknEvent;

@Service
public class NotifyUserCountUpdateJob extends EventJob {

    @Override
    protected Byte process(JknEvent event) {
        //TODO notify user updated
        return ES_DONE;
    }

    @Override
    protected Byte[] processType() {
        return new Byte[]{EV_USER_NOTIFY_COUNT};
    }


    @Override
    protected int maxLimit() {
        return 2000;
    }
}
