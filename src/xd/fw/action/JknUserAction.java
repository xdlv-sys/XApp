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

    String referrerName;


    public String obtainUsers(){
        int userId = jknUser == null ? -1 : jknUser.getUserId();
        String userName = jknUser == null ? null : jknUser.getUserName();

        total = jknService.getJknUserCount(userId,userName,referrerName);

        jknUsers = jknService.getJknUsers(userId,userName,referrerName, start, limit);
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

    public void setReferrerName(String referrerName) {
        this.referrerName = referrerName;
    }
}
