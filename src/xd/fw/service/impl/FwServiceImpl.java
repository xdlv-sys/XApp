package xd.fw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xd.fw.FwUtil;
import xd.fw.bean.*;
import xd.fw.bean.mapper.*;
import xd.fw.service.FwService;

import java.util.List;

@Service
public class FwServiceImpl extends MyBatisServiceImpl implements FwService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    ModMapper modMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RoleModMapper roleModMapper;
    @Autowired
    UserRoleMapper userRoleMapper;

    @Override
    public User userLogin(String name, String pwd) {
        return userMapper.selectUserByNameAndPwd(name, pwd);
    }

    @Override
    @Transactional
    public void saveOrUpdateUser(User user) {
        if (user.getId() != null) {
            User record = userMapper.selectByPrimaryKey(user.getId());
            if (!record.getPassword().equals(user.getPassword())){
                try {
                    user.setPassword(FwUtil.md5(user.getPassword()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            userMapper.updateByPrimaryKey(user);
        } else {
            user.setId(getPrimaryKey("t_user"));
            try {
                user.setPassword(FwUtil.md5(user.getPassword()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            userMapper.insert(user);
        }
        List<UserRole> userRoles = userRoleMapper.selectByUserId(user.getId());
        boolean isAdmin = userRoles != null
                && userRoles.size() > 0 && userRoles.get(0).getRoleid() == -2;
        if (isAdmin){
            return;
        }
        userRoleMapper.deleteByUserId(user.getId());
        if (user.getRoles() != null) {
            UserRole userRole;
            for (Role role : user.getRoles()) {
                userRole = new UserRole();
                userRole.setId(getPrimaryKey("t_userrole"));
                userRole.setRoleid(role.getId());
                userRole.setUserid(user.getId());
                userRoleMapper.insert(userRole);
            }
        }
    }

    @Override
    public List<User> getUsers(int start, int limit) {
        return userMapper.selectUsers(start, limit);
    }

    @Override
    public int getUsersCount() {
        return userMapper.selectUserCount();
    }


    @Override
    @Transactional
    public void deleteUserById(Integer id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Mod> getUserMods(Integer userId) {
        return modMapper.selectUserMods(userId);
    }

    @Override
    public List<Role> getRoles(int start, int limit) {
        return roleMapper.selectRoles(start, limit);
    }

    @Override
    public List<Role> getUserRoles(Integer userId) {
        return roleMapper.selectUserRoles(userId);
    }

    @Override
    @Transactional
    public void deleteRoleById(Integer id) {
        roleMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void saveOrUpdateRole(Role role) {
        if (role.getId() != null) {
            roleMapper.updateByPrimaryKey(role);
        } else {
            role.setId(getPrimaryKey("t_role"));
            roleMapper.insert(role);
        }
        roleModMapper.deleteByRoleId(role.getId());
        if (role.getMods() != null) {
            RoleMod roleMod;
            for (Mod mod : role.getMods()) {
                roleMod = new RoleMod();
                roleMod.setId(getPrimaryKey("t_rolemod"));
                roleMod.setModid(mod.getId());
                roleMod.setRoleid(role.getId());
                roleModMapper.insert(roleMod);
            }
        }
    }

    @Override
    @Transactional
    public void saveOrUpdateMod(Mod mod) {
        if (mod.getId() != null) {
            modMapper.updateByPrimaryKey(mod);
        } else {
            mod.setId(getPrimaryKey("t_mod"));
            modMapper.insert(mod);
        }
    }

    @Override
    @Transactional
    public void deleteMods(List<Mod> mods) {
        for (Mod mod : mods){
            modMapper.deleteByPrimaryKey(mod.getId());
        }
    }

    @Override
    public List<Mod> getModsByRole(Integer id) {
        return modMapper.selectModByRole(id);
    }

    @Override
    public int getRolesCount() {
        return roleMapper.selectRoleCount();
    }
}
