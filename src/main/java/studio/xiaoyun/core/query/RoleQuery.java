package studio.xiaoyun.core.query;

/**
 * 角色搜索参数
 */
public class RoleQuery extends AbstractQuery {
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
