package studio.xiaoyun.core.dao;

import studio.xiaoyun.core.exception.InvalidParameterException;

/**
 * 定义一些通用的dao层方法
 * @param <T> 实现该接口时需要指定具体的实体类
 */
public interface AbstractDao<T> {

    /**
     * 删除数据
     * @param entity 实体
     */
    void delete(T entity);

    /**
     * 删除数据
     * @param id 主键
     */
    void delete(String id);

    /**
     * 根据ID获得实体。
     * <p>会直接从数据库中查询数据
     * @param id 主键
     * @return 实体
     * @throws InvalidParameterException 如果根据id无法找到记录，则抛出该异常
     */
    T getById(String id) throws InvalidParameterException;

    /**
     * 根据ID获得实体的代理对象。
     * <p>只获得代理对象，在需要数据时才会查询数据库；如果id对应的实体不存在，该方法仍返回一个代理对象，
     * 但是在获得代理对象的属性时会抛出异常；代理对象只能在事务范围内使用
     * @param id 主键
     * @return id对应实体的代理对象
     */
    T loadById(String id);

    /**
     * 保存实体
     * @param entity 实体类
     * @return 实体类的id
     */
    String save(T entity);

    /**
     * 更新实体
     * @param entity 实体
     */
    void update(T entity);

}
