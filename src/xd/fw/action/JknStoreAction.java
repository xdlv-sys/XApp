package xd.fw.action;

import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.FwUtil;
import xd.fw.bean.JknEvent;
import xd.fw.bean.JknStoreApprove;
import xd.fw.service.JknService;

import java.sql.Timestamp;
import java.util.List;

public class JknStoreAction extends BaseAction {
    @Autowired
    JknService jknService;
    List<JknStoreApprove> jknStoreApproves;

    public String obtainStoreApproves(){
        total = jknService.getAllCount(JknStoreApprove.class);
        jknStoreApproves = jknService.getList(JknStoreApprove.class,null, start,limit);
        return SUCCESS;
    }

    public String approveStore(){
        FwUtil.safeEach(jknStoreApproves, new FwUtil.SafeEachProcess<JknStoreApprove>() {
            @Override
            public void process(JknStoreApprove jknStoreApprove) {
                jknStoreApprove.setApproveStatus(STATUS_DONE);
                jknService.updateApproveStore(jknStoreApprove);
            }
        });
        return FINISH;
    }

    public List<JknStoreApprove> getJknStoreApproves() {
        return jknStoreApproves;
    }

    public void setJknStoreApproves(List<JknStoreApprove> jknStoreApproves) {
        this.jknStoreApproves = jknStoreApproves;
    }
}
