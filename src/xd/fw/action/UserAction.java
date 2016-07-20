package xd.fw.action;


import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import xd.fw.FwException;
import xd.fw.FwUtil;
import xd.fw.bean.User;
import xd.fw.service.FwService;

import java.util.ArrayList;
import java.util.List;

public class UserAction extends BaseAction {
    @Autowired
    FwService fwService;

    User user;
    List<User> users;
    @Value("${web-name}")
    String name;

    @Value("${version}")
    String version;
    public String userLogin() throws Exception {
        User userRecord = fwService.userLogin(user.getName(), FwUtil.md5(user.getPassword()));
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

    public String saveUser() throws Exception {
        fwService.saveOrUpdateUser(user);
        return FINISH;
    }

    public String version(){
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

    public String getName() {
        return name;
    }
}
