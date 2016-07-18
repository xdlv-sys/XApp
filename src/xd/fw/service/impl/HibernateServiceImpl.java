package xd.fw.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;
import xd.fw.FwUtil;

import java.io.Serializable;
import java.util.List;

@Transactional(value = "transactionManager2", readOnly = true)
public class HibernateServiceImpl extends BaseServiceImpl {
    @Autowired
    protected HibernateTemplate htpl;
    
    @Override
    @Transactional("transactionManager2")
    public void saveOrUpdate(Object entity) {
        htpl.saveOrUpdate(entity);
    }

    private <T> String constructHsql(String prefix, Class<T> cls, T parms, String orderBy){
        StringBuffer hsql = new StringBuffer(prefix).append("from ").append(cls.getSimpleName());
        if (parms != null){
            final boolean[] appendWhere = new boolean[]{false};
            try {
                FwUtil.invokeBeanFields(parms, (f, o) -> {
                    if (o != null && StringUtils.isNotBlank(o.toString())) {
                        if (!appendWhere[0]) {
                            hsql.append(" where ").append(f.getName()).append("=").append(o);
                            appendWhere[0] = true;
                        } else {
                            hsql.append(" and ").append(f.getName()).append("=").append(o);
                        }
                    }
                });
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        if (StringUtils.isNotBlank(orderBy)){
            hsql.append(" order by ").append(orderBy);
        }
        return hsql.toString();
    }

    public <T> int getAllCount(Class<T> cls) {
        return getAllCount(cls,null);
    }

    public <T> int getAllCount(Class<T> cls, T params){
        List<Object> list = this.getList(constructHsql("select count(*) ",cls, params, null),-1,0);
        return ((Long) list.get(0)).intValue();
    }

    public <T> List<T> getList(Class<T> cls, String orderBy, int start, int limit){
        return getList(cls,null,orderBy,start,limit);
    }

    public <T> List<T> getList(Class<T> cls, T param, String orderBy, int start, int limit) {
        return this.getList(constructHsql("",cls,param,orderBy),start, limit);
    }

    public <T> List<T> getList(String hsql,int start, int limit){
        return htpl.execute(new HibernateCallback<List<T>>() {
            @Override
            public List<T> doInHibernate(Session session) throws HibernateException {
                Query query = session.createQuery(hsql);
                if (start > -1){
                    query.setFirstResult(start);
                }
                if (limit > 0){
                    query.setMaxResults(limit);
                }
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
