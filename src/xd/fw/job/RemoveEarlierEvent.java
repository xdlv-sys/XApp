package xd.fw.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xd.fw.bean.JknEvent;
import xd.fw.service.JknService;

@Service
public class RemoveEarlierEvent extends BaseJob {
    @Autowired
    JknService jknService;

    @Override
    public void doExecute() throws Exception {
        jknService.deleteEarlierEvent();
    }
}
