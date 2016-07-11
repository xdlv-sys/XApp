package xd.fw.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xd.fw.bean.HTest;
import xd.fw.service.HService;

import java.io.Serializable;

@Service
public class HServiceImpl extends HibernateServiceImpl implements HService{

    @Override
    @Transactional("transactionManager2")
    public void saveHTest(HTest hTest) {
        htpl.save(hTest);
    }

    @Override
    @Transactional("transactionManager2")
    public void saveOrUpdate(Object obj) {
        htpl.saveOrUpdate(obj);
    }

    @Override
    @Transactional("transactionManager2")
    public void update(Object obj) {
        htpl.update(obj);
    }

    @Override
    public <T> T get(Class<T> cls, Serializable id) {
        return htpl.get(cls,id);
    }
}
