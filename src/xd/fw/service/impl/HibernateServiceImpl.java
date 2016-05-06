package xd.fw.service.impl;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value="transactionManager2",readOnly=true)
public class HibernateServiceImpl extends BaseServiceImpl{
}
