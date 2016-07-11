package xd.fw.service;

import xd.fw.bean.HTest;

import java.io.Serializable;

/**
 * Created by xd on 2016/5/6.
 */
public interface HService extends BaseService{

    void saveHTest(HTest hTest);

    void saveOrUpdate(Object obj);

    void update(Object obj);

    <T> T get(Class<T> cls, Serializable id);

}
