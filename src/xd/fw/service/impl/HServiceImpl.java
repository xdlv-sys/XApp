package xd.fw.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xd.fw.bean.HTest;
import xd.fw.service.HService;

@Service
public class HServiceImpl extends HibernateServiceImpl implements HService{

    @Override
    @Transactional("transactionManager2")
    public void saveHTest(HTest hTest) {
        htpl.save(hTest);
    }
}
