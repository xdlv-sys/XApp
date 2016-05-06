package xd.fw.service.impl;

import org.springframework.transaction.annotation.Transactional;

@Transactional(value="transactionManager",readOnly=true)
public class MyBatisServiceImpl extends BaseServiceImpl{
}
