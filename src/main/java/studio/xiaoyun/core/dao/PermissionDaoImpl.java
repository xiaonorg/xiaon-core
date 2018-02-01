package studio.xiaoyun.core.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.pojo.PermissionDO;
import studio.xiaoyun.core.query.PermissionQuery;

import java.util.List;

@Repository("permissionDao")
public class PermissionDaoImpl extends AbstractDaoImpl<PermissionDO> implements PermissionDao {

    @Override
    public PermissionDO getById(String id) throws InvalidParameterException {
        PermissionDO permission = getSession().get(PermissionDO.class, id);
        if(permission==null){
            throw new InvalidParameterException(id+"不存在");
        }
        return permission;
    }

    @Override
    public PermissionDO loadById(String id) {
        return getSession().load(PermissionDO.class, id);
    }

    @Override
    public List<PermissionDO> listPermissionByParameter(PermissionQuery parameter) {
        return listByParameter(null,null,parameter,PermissionDO.class);
    }

    @Override
    public long countPermissionByParameter(PermissionQuery parameter) {
        return countByParameter(null,null,parameter);
    }

    @Override
    public List<PermissionDO> listPermissionByUserId(String userId, PermissionQuery parameter) {
        StringBuilder where = new StringBuilder();
        where.append("permission_0.permissionId in (SELECT a.permissionId FROM xy_permission AS a JOIN xy_user_permission");
        where.append(" AS b ON a.permissionId = b.permissionId WHERE b.userId ='").append(userId);
        where.append("' union SELECT a.permissionId FROM xy_permission AS a JOIN xy_role_permission AS b ON ");
        where.append("a.permissionId = b.permissionId JOIN xy_user_role AS c ON b.roleId = c.roleId WHERE c.userId ='");
        where.append(userId).append("')");
        return listByParameter(null,where.toString(),parameter,PermissionDO.class);
    }

    @Override
    public long countPermissionByUserId(String userId, PermissionQuery parameter) {
        StringBuilder where = new StringBuilder();
        where.append("permission_0.permissionId in (SELECT a.permissionId FROM xy_permission AS a JOIN xy_user_permission");
        where.append(" AS b ON a.permissionId = b.permissionId WHERE b.userId ='").append(userId);
        where.append("' union SELECT a.permissionId FROM xy_permission AS a JOIN xy_role_permission AS b ON ");
        where.append("a.permissionId = b.permissionId JOIN xy_user_role AS c ON b.roleId = c.roleId WHERE c.userId ='");
        where.append(userId).append("')");
        return countByParameter(null,where.toString(),parameter);
    }

    @Override
    public List<PermissionDO> listPermissionByRoleId(String roleId, PermissionQuery parameter) {
        String table = "join xy_role_permission as role_permission_0 on permission_0.permissionId=role_permission_0.permissionId";
        String where = "role_permission_0.roleId='"+roleId+"'";
        return listByParameter(table,where,parameter,PermissionDO.class);
    }

    @Override
    public long countPermissionByRoleId(String roleId, PermissionQuery parameter) {
        String table = "join xy_role_permission as role_permission_0 on permission_0.permissionId=role_permission_0.permissionId";
        String where = "role_permission_0.roleId='"+roleId+"'";
        return countByParameter(table,where,parameter);
    }

    @Override
    String getQuerySql() {
        return "select permission_0.* from xy_permission as permission_0";
    }

}
