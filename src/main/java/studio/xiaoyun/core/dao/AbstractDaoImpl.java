package studio.xiaoyun.core.dao;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import studio.xiaoyun.core.exception.NotImplementedException;
import studio.xiaoyun.core.exception.XyException;
import studio.xiaoyun.core.query.AbstractQuery;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 定义一些通用的DAO层方法
 * @param <T> 实现该接口时需要指定具体的实体类
 */
public abstract class AbstractDaoImpl<T> implements AbstractDao<T> {
    @Resource
    private SessionUtil sessionUtil;

    @Override
    public void delete(T entity) {
        Session session = this.getSession();
        session.delete(entity);
    }

    @Override
    public void delete(String id) {
        Session session = this.getSession();
        T t = this.loadById(id);
        session.delete(t);
    }

    /**
     * 根据HQL语句查询数据
     * @param hql hql语句，例如：from Role where name='aa'
     * @param resultType 返回值的类型
     * @return 查询结果
     */
    <F> List<F> listByHQL(String hql, Class<F> resultType) {
        Query<F> query = getSession().createQuery(hql,resultType);
        return query.list();
    }

    /**
     * 根据SQL语句查询数据，结果不是实体，是Object列表
     * @param sql sql语句
     * @return 查询结果
     */
    List<Object> listBySQL(String sql) {
        NativeQuery query = getSession().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Object> list = query.list();
        return list;
    }

    /**
     * 根据sql语句取得实体
     * @param <F>    返回值的类型
     * @param sql    完整的sql语句，需要有'*'号，例如：select alias.* from table as alias
     * @param alias  sql语句中表的别名，例如：alias
     * @param resultType 返回值的类型
     * @return 实体列表
     */
    <F> List<F> listBySQL(String sql, String alias, Class<F> resultType) {
        NativeQuery query = getSession().createNativeQuery(sql);
        query.addEntity(alias,resultType);
        @SuppressWarnings("unchecked")
        List<F> list = query.list();
        return list;
    }

    /**
     * 根据HQL语句查询数据的数量
     * @param hql hql语句
     * @return 数量
     */
    long countByHQL(String hql) {
        Query query = getSession().createQuery(hql);
        return (Long) query.uniqueResult();
    }

    /**
     * 根据SQL语句查询数据的数量
     * @param sql sql语句
     * @return 数量
     */
    long countBySQL(String sql) {
        NativeQuery query = getSession().createNativeQuery(sql);
        BigInteger count = (BigInteger) query.uniqueResult();
        return count.longValue();
    }

    /**
     * 获得当前事务中的session
     * @return session
     */
    Session getSession() {
        return sessionUtil.getSession();
    }

    @Override
    public String save(T entity) {
        Session session = this.getSession();
        return (String)session.save(entity);
    }

    @Override
    public void update(T entity) {
        Session session = this.getSession();
        session.update(entity);
    }

    /**
     * 根据参数类查询记录的数量
     * @param table    拼接表的sql语句，例如：join table_b on table_a.id=table_b.id，可以为null
     * @param where     拼接where条件的sql语句，例如：table_a.id=1 and table_b.name like 'a%'，可以为null
     * @param parameter 封装的查询条件，可以为null
     * @return 数量
     * @see #getQuerySql()
     */
    long countByParameter(String table, String where, AbstractQuery parameter){
        String sql = buildSqlForCount(table,where,parameter);
        NativeQuery query = getSession().createNativeQuery(sql);
        if(parameter!=null){
            List<Object> list = parameter.getQueryValue();
            for(int i=0;i<list.size();i++){
                query.setParameter(i+1,list.get(i));
            }
        }
        BigInteger count = (BigInteger) query.uniqueResult();
        return count.longValue();
    }

    /**
     * 根据参数类查询符合条件的所有记录
     * @param table    拼接表的sql语句，例如：join table_b on table_a.id=table_b.id，可以为null
     * @param where     拼接where条件的sql语句，例如：table_a.id=1 and table_b.name like 'a%'，可以为null
     * @param parameter 封装的查询条件，可以为null
     * @param clazz  实体类，不能为null
     * @return 记录的列表
     * @see #getQuerySql()
     */
    <F> List<F> listByParameter(String table, String where, AbstractQuery parameter, Class<F> clazz){
        String sql = buildSqlForList(table,where,parameter);
        String alias = getQuerySqlAlias();
        NativeQuery query = getSession().createNativeQuery(sql);
        if(parameter!=null){
            List<Object> list = parameter.getQueryValue();
            for(int i=0;i<list.size();i++){
                query.setParameter(i+1,list.get(i));
            }
            query.setFirstResult(parameter.getFirstResult());
            query.setMaxResults(parameter.getMaxResults());
        }
        query.addEntity(alias, clazz);
        @SuppressWarnings("unchecked")
        List<F> list = query.list();
        return list;
    }

    /**
     * 构建查询数量的sql语句
     * @return 完整的查询sql，例如：select count(*) from table as table_a where table_a.id=1
     */
    private String buildSqlForCount(String table, String where, AbstractQuery parameter) {
        Map<String, String> mapping = getMapping(parameter);
        String querySql = getQuerySql();
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) ");
        sql.append(querySql.substring(querySql.indexOf('.') + 2));
        if (StringUtils.isNotBlank(table)) {
            sql.append(" ");
            sql.append(table);
        }
        if (StringUtils.isNotBlank(where)) {
            sql.append(" where ").append(where);
        }
        if (parameter != null) {
            String whereStr = parameter.getQuery(mapping);
            if (StringUtils.isNotBlank(whereStr)) {
                if (StringUtils.isBlank(where)) {
                    sql.append(" where ");
                }else{
                    sql.append(" and ");
                }
                sql.append(whereStr);
            }
        }
        return sql.toString();
    }

    /**
     * 构建查询数据列表的sql语句
     * @return 完整的查询sql，例如：select count(*) from table as table_a where table_a.id=1
     */
    private String buildSqlForList(String table, String where, AbstractQuery parameter) {
        Map<String, String> mapping = getMapping(parameter);
        StringBuilder sql = new StringBuilder();
        //拼接表
        sql.append(getQuerySql());
        if (StringUtils.isNotBlank(table)) {
            sql.append(" ");
            sql.append(table);
        }
        //拼接查询条件
        if (StringUtils.isNotBlank(where)) {
            sql.append(" where ").append(where);
        }
        if (parameter != null) {
            String whereStr = parameter.getQuery(mapping);
            if (StringUtils.isNotBlank(whereStr)) {
                if (StringUtils.isNotBlank(where)) {
                    sql.append(" and ");
                }else{
                    sql.append(" where ");
                }
                sql.append(whereStr);
            }
            List<String> sortFields = parameter.getSortField();
            List<Boolean> isAscs = parameter.getIsAsc();
            StringBuilder orderStr = new StringBuilder();
            for (int i = 0; i < sortFields.size(); i++) {
                orderStr.append(mapping.get(sortFields.get(i))).append(isAscs.get(i) ? " asc ," : " desc ,");
            }
            if (orderStr.length() > 0) {
                sql.append(" order by ").append(orderStr.deleteCharAt(orderStr.length() - 1));
            }
        }
        return sql.toString();
    }

    /**
     * 获得搜索类中属性名和数据表中列名的映射关系。
     * <p>默认搜索类中属性名和数据表中列名相同，但子类中也可以重写</p>
     * @return 搜索类中属性名和数据表中列名的映射关系，key:搜索类的属性名，value:数据表中列的名称，不是实体类的属性名
     */
    Map<String, String> getMapping() {
        return new HashMap<>();
    }

    /**
     * 获得搜索类中属性名和数据表中列名的映射关系。
     * <p>默认搜索类中属性名和数据表中列名相同</p>
     * @param parameter 搜索参数,可以为null
     * @return 搜索类中属性名和数据表中列名的映射关系，key:搜索类的属性名，value:数据表中列的名称，不是实体类的属性名
     */
    private Map<String, String> getMapping(AbstractQuery parameter) {
        Map<String, String> mapping = getMapping();
        if (!mapping.isEmpty()) {  //如果子类中已经指定映射关系，则直接返回结果
            return mapping;
        }
        if (parameter == null) {
            return mapping;
        }
        String alias = getQuerySqlAlias();
        Set<String> names = parameter.getPropertyName();
        for (String name : names) {
            mapping.put(name, alias + name);
        }
        return mapping;
    }

    /**
     * 从查询sql语句中截取出表的别名
     * @return 表的别名
     */
    private String getQuerySqlAlias(){
        String sql = getQuerySql();
        Matcher matcher = Pattern.compile("^select\\s+[0-9A-Za-z_-]+[.][*]", Pattern.CASE_INSENSITIVE).matcher(sql);
        if (matcher.find()) {
            return matcher.group().substring(6, matcher.group().length() - 2).trim() + '.';
        } else {
            throw new XyException("sql应该以“select 别名.*”开头，" + sql);
        }
    }

    /**
     * 获得查询数据列表的sql语句。
     * <p>sql应该以“select 别名.*”开头，例如：select table_0.* from table as table_0</p>
     * @return 查询数据列表的sql语句。
     */
    String getQuerySql() {
        throw new NotImplementedException();
    }

}