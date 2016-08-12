package xd.fw.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.fw.bean.JknEvent;
import xd.fw.service.JknService;

import java.util.Map;

@Service
public class UserSettlementApply implements UserHandler {
    @Autowired
    JknService jknService;

    @Override
    public byte process(JknEvent event, Map<Integer, UserDesc> userMap) {
        return jknService.applySettlement(event.getDbKey());
    }
}
