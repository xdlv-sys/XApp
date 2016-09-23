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

    public List<JknStoreApprove> getJknStoreApproves() {
        return jknStoreApproves;
    }
}
