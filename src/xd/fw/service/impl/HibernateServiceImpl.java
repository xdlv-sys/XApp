package xd.fw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value="transactionManager2",readOnly=true)
public class HibernateServiceImpl extends BaseServiceImpl{
    @Autowired
    protected HibernateTemplate htpl;
}
