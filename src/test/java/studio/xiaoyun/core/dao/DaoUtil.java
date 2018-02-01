package studio.xiaoyun.core.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.pojo.RoleDO;
import studio.xiaoyun.core.query.AbstractQuery;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class DaoUtil extends AbstractDaoImpl<RoleDO>{
    @Resource
    private RoleDao roleDao;

    @Override
    public RoleDO getById(String id) throws InvalidParameterException {
        return roleDao.getById(id);
    }

    @Override
    public RoleDO loadById(String id) {
        return roleDao.loadById(id);
    }

    @Override
    public void delete(RoleDO entity) {
        super.delete(entity);
    }

    @Override
    public void delete(String id) {
        super.delete(id);
    }

    @Override
    public  <F> List<F> listByHQL(String hql, Class<F> resultType) {
        return super.listByHQL(hql, resultType);
    }

    @Override
    public List<Object> listBySQL(String sql) {
        return super.listBySQL(sql);
    }

    @Override
    public <F> List<F> listBySQL(String sql, String alias, Class<F> resultType) {
        return super.listBySQL(sql, alias, resultType);
    }

    @Override
    public long countByHQL(String hql) {
        return super.countByHQL(hql);
    }

    @Override
    public long countBySQL(String sql) {
        return super.countBySQL(sql);
    }

    @Override
    public String save(RoleDO entity) {
        return super.save(entity);
    }

    @Override
    public void update(RoleDO entity) {
        super.update(entity);
    }

    @Override
    public long countByParameter(String table, String where, AbstractQuery parameter) {
        return super.countByParameter(table, where, parameter);
    }

    @Override
    public  <F> List<F> listByParameter(String table, String where, AbstractQuery parameter, Class<F> clazz) {
        return super.listByParameter(table, where, parameter, clazz);
    }

    @Override
    public Map<String, String> getMapping() {
        return super.getMapping();
    }

    @Override
    public String getQuerySql() {
        return super.getQuerySql();
    }
}
