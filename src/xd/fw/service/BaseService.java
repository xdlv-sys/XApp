package xd.fw.service;

import java.io.Serializable;
import java.util.List;

public interface BaseService {

    int getAllCount();

	int getAllCount(Class<?> cls);

    <T> List<T> getList(Class<T> cls, String orderBy, int start, int limit);

	int getPrimaryKey(String tableName);

	void saveOrUpdate(Object entity);

	<T> T get(Class<T> tClass,Serializable id);

	//<T> T load(Class<T> tClass,Serializable id);

	void update(Object entity);

	void save(Object entity);

	void merge(Object entity);

	void delete (Object entity);

    <T> List<T> getList(String hsql);
}
