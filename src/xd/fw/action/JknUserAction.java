package xd.fw.action;

import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.bean.JknUser;
import xd.fw.service.JknService;

import java.util.List;

public class JknUserAction extends BaseAction {
    @Autowired
    JknService jknService;
    JknUser jknUser;
    List<JknUser> jknUsers;

    public String obtainUsers(){
        total = jknService.getAllCount(JknUser.class);
        jknUsers = jknService.getList(JknUser.class,null, start,limit);
        return SUCCESS;
    }

    public void setJknUser(JknUser jknUser) {
        this.jknUser = jknUser;
    }

    public JknUser getJknUser() {
        return jknUser;
    }

    public List<JknUser> getJknUsers() {
        return jknUsers;
    }

    public void setJknUsers(List<JknUser> jknUsers) {
        this.jknUsers = jknUsers;
    }
}
