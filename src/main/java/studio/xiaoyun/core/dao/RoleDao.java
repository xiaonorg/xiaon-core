package studio.xiaoyun.core.dao;

import studio.xiaoyun.core.pojo.RoleDO;
import studio.xiaoyun.core.query.RoleQuery;

import java.util.List;

/**
 * 角色
 */
public interface RoleDao extends AbstractDao<RoleDO> {

    /**
     * 获得角色的数量
     * @param parameter 搜索参数
     * @return 角色的数量
     */
    long countRoleByParameter(RoleQuery parameter);

    /**
     * 搜索角色
     * @param parameter 搜索参数
     * @return 角色列表
     */
    List<RoleDO> listRoleByParameter(RoleQuery parameter);

    /**
     * 根据用户id获得该用户拥有的角色的数量
     * @param userId 用户id
     * @param parameter 搜索参数
     * @return 用户拥有的角色的数量
     */
    long countRoleByUserId(String userId, RoleQuery parameter);

    /**
     * 根据用户id获得该用户拥有的角色
     * @param userId 用户id
     * @param parameter 搜索参数
     * @return 用户拥有的角色
     */
    List<RoleDO> listRoleByUserId(String userId, RoleQuery parameter);

}
