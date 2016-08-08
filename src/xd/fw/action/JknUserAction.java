package xd.fw.action;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import xd.fw.FwUtil;
import xd.fw.bean.JknUser;
import xd.fw.service.ConstructHql;
import xd.fw.service.JknService;
import xd.fw.service.SetParameters;

import java.lang.reflect.Field;
import java.util.List;

public class JknUserAction extends BaseAction {
    @Autowired
    JknService jknService;
    JknUser jknUser;
    List<JknUser> jknUsers;

    public String obtainUsers(){

        ConstructHql<JknUser> constructHql = new ConstructHql<JknUser>() {
            @Override
            public String process(JknUser user) {
                StringBuffer buffer = new StringBuffer("from JknUser where 1=1");
                if (jknUser != null && StringUtils.isNotBlank(jknUser.getUserName())){
                    buffer.append(" and userName like :userName ");
                }
                if (jknUser != null && jknUser.getReferrer() != null){
                    buffer.append(" and referrer=:referrer");
                }
                return buffer.toString();
            }
        };

        SetParameters setParameters = new SetParameters() {
            @Override
            public void process(Query query) {
                if (jknUser != null && StringUtils.isNotBlank(jknUser.getUserName())){
                    query.setString("userName", "%" + jknUser.getUserName() + "%");
                }
                if (jknUser != null && jknUser.getReferrer() != null){
                    query.setInteger("referrer", jknUser.getReferrer());
                }
            }
        };
        total = jknService.getAllCount(jknUser,setParameters,constructHql);
        jknUsers = jknService.getLists("",jknUser,setParameters,constructHql,start,limit);
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
