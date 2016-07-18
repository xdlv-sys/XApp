package xd.fw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import xd.fw.bean.PrimaryKey;

import xd.fw.bean.mapper.PrimaryKeyMapper;
import xd.fw.service.BaseService;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseServiceImpl implements BaseService {

    @Autowired
    PrimaryKeyMapper primaryKeyMapper;

    static Map<String, int[]> primaryKeyMap = new HashMap<String, int[]>();
    final static int PRE_OCCUPY_MAX = 10;

    public void setPrimaryKeyMapper(PrimaryKeyMapper primaryKeyMapper) {
        this.primaryKeyMapper = primaryKeyMapper;
    }

    @Override
    public int getAllCount() {
        return 0;
    }

    @Override
    public int getPrimaryKey(String tableName) {
        int[] value;
        synchronized (primaryKeyMap) {
            value = primaryKeyMap.get(tableName);
            if (value == null || value[0] == value[1]) {
                PrimaryKey primaryKey = primaryKeyMapper.selectByPrimaryKey(tableName);
                if (primaryKey == null) {
                    value = new int[]{1, PRE_OCCUPY_MAX};
                    primaryKeyMapper.insert(new PrimaryKey(tableName, PRE_OCCUPY_MAX));
                } else {
                    value = new int[]{primaryKey.getCurrentId() + 1, primaryKey.getCurrentId() + PRE_OCCUPY_MAX};
                    primaryKeyMapper.updateByPrimaryKey(new PrimaryKey(tableName, value[1]));
                }
                primaryKeyMap.put(tableName, value);
            } else {
                value[0] += 1;
            }
        }
        return value[0];
    }

    public void saveOrUpdate(Object entity) {
        throw new RuntimeException("no implementation");
    }

    @Override
    public <T> int getAllCount(Class<T> cls) {
        throw new RuntimeException("no implementation");
    }

    @Override
    public <T> int getAllCount(Class<T> cls, T params) {
        throw new RuntimeException("no implementation");
    }

    @Override
    public <T> List<T> getList(Class<T> cls, T param, String orderBy, int start, int limit) {
        throw new RuntimeException("no implementation");
    }

    @Override
    public <T> List<T> getList(Class<T> cls, String orderBy, int start, int limit) {
        throw new RuntimeException("no implementation");
    }

    @Override
    public <T> T get(Class<T> tClass, Serializable id) {
        throw new RuntimeException("no implementation");
    }

    @Override
    public void update(Object entity) {
        throw new RuntimeException("no implementation");
    }

    @Override
    public void save(Object entity) {
        throw new RuntimeException("no implementation");
    }

    @Override
    public void merge(Object entity) {
        throw new RuntimeException("no implementation");
    }

    @Override
    public void delete(Object entity) {
        throw new RuntimeException("no implementation");
    }

    @Override
    public <T> List<T> getList(String hsql) {
        throw new RuntimeException("no implementation");
    }


}