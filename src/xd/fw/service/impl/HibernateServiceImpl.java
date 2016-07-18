package xd.fw.service.impl;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Transactional(value = "transactionManager2", readOnly = true)
public class HibernateServiceImpl extends BaseServiceImpl {
    @Autowired
    protected HibernateTemplate htpl;

    public void saveOrUpdate(Object entity) {
        htpl.saveOrUpdate(entity);
    }

    public int getAllCount(Class<?> cls) {
        return htpl.execute(new HibernateCallback<Integer>() {
            @Override
            public Integer doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("select count(*) from " + cls.getSimpleName());
                List list = query.list();
                return ((Long) list.get(0)).intValue();
            }
        });
    }

    public <T> List<T> getList(Class<T> cls, String orderBy, int start, int limit){
        return htpl.execute(new HibernateCallback<List<T>>() {
            @Override
            public List<T> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery("from " + cls.getSimpleName()
                        + orderBy == null ? "" : "order by " + orderBy);
                query.setFirstResult(start);
                query.setMaxResults(limit);
                return query.list();
            }
        });
    }

    public <T> List<T> getList(String hsql){
        return htpl.execute(new HibernateCallback<List<T>>() {
            @Override
            public List<T> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hsql);
                return query.list();
            }
        });
    }

    public <T> T get(Class<T> tClass, Serializable id) {
        return htpl.get(tClass, id);
    }


    protected <T> T load(Class<T> tClass, Serializable id) {
        return htpl.load(tClass, id);
    }

    @Override
    @Transactional("transactionManager2")
    public void update(Object entity) {
        htpl.update(entity);
    }

    @Override
    @Transactional("transactionManager2")
    public void save(Object entity) {
        htpl.save(entity);
    }

    @Override
    @Transactional("transactionManager2")
    public void merge(Object entity) {
        htpl.merge(entity);
    }

    @Override
    @Transactional("transactionManager2")
    public void delete(Object entity) {
        htpl.delete(entity);
    }
}
