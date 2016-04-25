package xd.fw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xd.fw.bean.Mod;
import xd.fw.bean.Role;
import xd.fw.bean.User;

import java.util.List;

public interface FwService extends BaseService{

    User userLogin(String name, String pwd);

    List<User> getUsers(int start, int limit);

    int getUsersCount();

    void saveOrUpdateUser(User user);

    void deleteUserById(Integer id);

    List<Mod> getUserMods(Integer userId);

    int getRolesCount();

    List<Role> getRoles(int start, int limit);

    void deleteRoleById(Integer id);

    void saveOrUpdateRole(Role role);

    void saveOrUpdateMod(Mod mod);

    List<Mod> getModsByRole(Integer roleId);

    List<Role> getUserRoles(Integer userId);

    void deleteMods(List<Mod> mods);
}
