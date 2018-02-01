package studio.xiaoyun.core.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.pojo.RoleDO;
import studio.xiaoyun.core.query.RoleQuery;

import java.util.List;

@Repository("roleDao")
public class RoleDaoImpl extends AbstractDaoImpl<RoleDO> implements RoleDao {

    @Override
    public RoleDO getById(String id) throws InvalidParameterException {
        RoleDO role = getSession().get(RoleDO.class,id);
        if(role==null){
            throw new InvalidParameterException(id+"不存在");
        }
        return role;
    }

    @Override
    public RoleDO loadById(String id) {
        return getSession().load(RoleDO.class,id);
    }

    @Override
    String getQuerySql() {
        return "select role_0.* from xy_role as role_0";
    }

    @Override
    public long countRoleByParameter(RoleQuery parameter) {
        return countByParameter(null,null,parameter);
    }

    @Override
    public List<RoleDO> listRoleByParameter(RoleQuery parameter) {
        return listByParameter(null,null,parameter,RoleDO.class);
    }

    @Override
    public long countRoleByUserId(String userId, RoleQuery parameter) {
        String table = "join xy_user_role as user_role_0 on user_role_0.roleId=role_0.roleId";
        String where = "user_role_0.userId='"+userId+"'";
        return countByParameter(table,where,parameter);
    }

    @Override
    public List<RoleDO> listRoleByUserId(String userId, RoleQuery parameter) {
        String table = "join xy_user_role as user_role_0 on user_role_0.roleId=role_0.roleId";
        String where = "user_role_0.userId='"+userId+"'";
        return listByParameter(table,where,parameter,RoleDO.class);
    }
}
