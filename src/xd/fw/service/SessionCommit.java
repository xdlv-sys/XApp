package xd.fw.service;

import org.springframework.orm.hibernate4.HibernateTemplate;

public interface SessionCommit {
    void process(HibernateTemplate htpl);
/*
    default <T> T get(Class<T> cls, Serializable id){
        return null;
    }*/

}
