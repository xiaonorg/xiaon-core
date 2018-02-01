package studio.xiaoyun.core.dao;

import studio.xiaoyun.core.pojo.PermissionDO;
import studio.xiaoyun.core.query.PermissionQuery;

import java.util.List;

/**
 * 权限相关
 */
public interface PermissionDao extends AbstractDao<PermissionDO> {

    /**
     * 获得权限信息
     * @param parameter 搜索参数
     * @return 权限
     */
    List<PermissionDO> listPermissionByParameter(PermissionQuery parameter);

    /**
     * 权限的数量
     * @param parameter　搜索参数
     * @return 权限的数量
     */
    long countPermissionByParameter(PermissionQuery parameter);

    /**
     * 根据用户Id获得该用户的权限
     * @param userId 用户Id
     * @param parameter 搜索参数
     * @return 用户的权限
     */
    List<PermissionDO> listPermissionByUserId(String userId, PermissionQuery parameter);

    /**
     * 根据用户Id获得该用户的权限的数量
     * @param userId　用户Id
     * @param parameter　搜索参数
     * @return 该用户的权限的数量
     */
    long countPermissionByUserId(String userId, PermissionQuery parameter);

    /**
     * 根据角色Id获得该角色的权限
     * @param roleId 角色Id
     * @param parameter 搜索参数
     * @return 角色的权限
     */
    List<PermissionDO> listPermissionByRoleId(String roleId, PermissionQuery parameter);

    /**
     * 根据角色Id获得该角色的权限的数量
     * @param roleId　角色Id
     * @param parameter　搜索参数
     * @return 该角色的权限的数量
     */
    long countPermissionByRoleId(String roleId, PermissionQuery parameter);

}
