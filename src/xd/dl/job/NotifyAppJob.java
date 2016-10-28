package xd.dl.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xd.dl.DlConst;
import xd.dl.bean.DlOrder;
import xd.fw.bean.Event;
import xd.fw.job.EventJob;

import java.sql.Timestamp;

/**
 * Created by xd lv on 10/27/2016.
 */
//@Service
public class NotifyAppJob extends EventJob implements DlConst{

    @Override
    @Scheduled(cron="0/2 * * * * ?")
    public void execute() throws Exception {
        super.execute();
    }

    @Override
    protected byte process(Event event) throws Exception {
        //TODO notify app
        return STATUS_DONE;
    }

    @Override
    protected void processSuccess(Event event) {
        super.processSuccess(event);
        // set order status
        DlOrder dlOrder = fwService.get(DlOrder.class, event.getDbContent());
        dlOrder.setNotifyStatus(STATUS_DONE);
        fwService.update(dlOrder);
    }

    @Override
    protected void processFailure(Event event) {
        //trigger again 5 seconds later
        event.setTriggerDate(new Timestamp(System.currentTimeMillis() + 5 * 1000));
        super.processFailure(event);
    }

    @Override
    protected byte[] processType() {
        return new byte[]{ NOTIFY_APP };
    }

    @Override
    protected int maxTry() {
        return 3;
    }
}
