package studio.xiaoyun.core.service;

import studio.xiaoyun.core.pojo.RoleDO;

import java.util.List;

/**
 * 角色相关
 */
public interface RoleService {

    /**
     * 设置角色拥有的权限
     * @param roleId 角色id
     * @param permissionIds 权限id,为null表示删除角色拥有的所有权限
     */
    void setRolePermission(String roleId, List<String> permissionIds);

    /**
     * 根据角色id删除角色
     * @param roleId 角色id
     */
    void deleteRole(List<String> roleId);

    /**
     *  更新角色信息。
     *  <p>只能更新名称和描述</p>
     * @param role 角色信息
     */
    void updateRole(RoleDO role);

    /**
     * 创建角色
     * @param name 角色的名称
     * @param description 角色的描述
     * @return 新角色的id
     */
    String saveRole(String name, String description);
}
