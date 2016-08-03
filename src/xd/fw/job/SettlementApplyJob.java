package xd.fw.job;

import org.springframework.stereotype.Service;
import xd.fw.bean.JknEvent;

@Service
public class SettlementApplyJob extends EventJob {

    @Override
    protected Byte[] processType() {
        return new Byte[]{EV_USER_SETTLEMENT_APPLY};
    }

    @Override
    protected Byte process(JknEvent event) {
        return jknService.applySettlement(event.getDbKey());

    }
}
