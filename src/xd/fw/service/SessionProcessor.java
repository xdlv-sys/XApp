package xd.fw.service;

import org.springframework.orm.hibernate4.HibernateTemplate;

public interface SessionProcessor<T> {
    T process(HibernateTemplate htpl);
}
