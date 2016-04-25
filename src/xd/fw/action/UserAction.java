package xd.fw.action;


import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import xd.fw.FwException;
import xd.fw.I18n;
import xd.fw.bean.User;
import xd.fw.service.FwService;

import java.util.ArrayList;
import java.util.List;
@Controller
public class UserAction extends BaseAction {
    @Autowired
    FwService fwService;

    User user;
    List<User> users;
    String version;
    public String userLogin() {
        User userRecord = fwService.userLogin(user.getName(), user.getPassword());
        if (userRecord != null) {
            users = new ArrayList<User>();
            users.add(userRecord);
            //add session
            ServletActionContext.getRequest().getSession().setAttribute(USER,userRecord);
        } else {
            throw new FwException("用户名或密码不正确");
        }
        return SUCCESS;
    }

    public String deleteUser() {
        for (int i=0; users != null && i<users.size();i++){
            fwService.deleteUserById(users.get(i).getId());
        }
        return FINISH;
    }

    public String obtainUsers() {
        total = fwService.getUsersCount();
        users = fwService.getUsers(start, limit);
        return SUCCESS;
    }

    public String saveUser() {
        fwService.saveOrUpdateUser(user);
        return FINISH;
    }

    public String version(){
        version = I18n.getI18n("version");
        return SUCCESS;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getVersion() {
        return version;
    }

}
