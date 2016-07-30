package xd.fw.service.impl;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xd.fw.FwUtil;
import xd.fw.bean.*;
import xd.fw.bean.mapper.*;
import xd.fw.service.FwService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FwServiceImpl extends HibernateServiceImpl implements FwService {

    @Override
    public User userLogin(User user) throws Exception {
        List<User> users = htpl.execute(new HibernateCallback<List<User>>() {
            @Override
            public List<User> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("from User where name=:name");
                query.setParameter("name",user.getName());
                return query.list();
            }
        });
        User ret;
        if (users.size() > 0
                && (ret = users.get(0)).getPassword().equals(FwUtil.md5(user.getPassword()))){
            return ret;
        }
        return null;
    }

    @Override
    @Transactional
    public void saveOrUpdateUser(User user) throws Exception {
        User record;
        if (user.getId() != null) {
            record = get(User.class, user.getId());
            if (!record.getPassword().equals(user.getPassword())){
                user.setPassword(FwUtil.md5(user.getPassword()));
            }
            //merge(user);
        } else {
            user.setId(getPrimaryKey(User.class));
            user.setPassword(FwUtil.md5(user.getPassword()));
            //save(user);
        }
        for (Role role : user.getRolesL()){
            role = load(Role.class,role.getId());
        }
        user.setRoles(new HashSet<>(user.getRolesL()));
        merge(user);
    }

    @Override
    @Transactional
    public void saveOrUpdateRole(Role role) {
        if (role.getId() == null){
            role.setId(getPrimaryKey(Role.class));
        }
        for (Mod mod : role.getModsL()){
            mod = load(Mod.class, mod.getId());
        }
        role.setMods(new HashSet<>(role.getModsL()));

        saveOrUpdate(role);
    }
}
