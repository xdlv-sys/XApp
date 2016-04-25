package xd.fw.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import xd.fw.bean.Mod;
import xd.fw.bean.Role;
import xd.fw.bean.User;
import xd.fw.service.FwService;

import java.util.List;

@Controller
public class RoleAction extends BaseAction {
    @Autowired
    FwService fwService;
    Role role;
    List<Role> roles;

    User user;

    public String obtainRoles() {
        total = fwService.getRolesCount();
        roles = fwService.getRoles(start, limit);
        return SUCCESS;
    }

    public String obtainUserRoles() {
        roles = fwService.getUserRoles(user.getId());
        return SUCCESS;
    }

    public String deleteRole() {
        for (int i=0; roles != null && i<roles.size();i++){
            fwService.deleteRoleById(roles.get(i).getId());
        }
        return FINISH;
    }

    public String saveRole() {
        fwService.saveOrUpdateRole(role);
        return FINISH;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
