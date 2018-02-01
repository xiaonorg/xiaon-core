package studio.xiaoyun.core.dto;

/**
 * 角色
 */
public class RoleDTO implements AbstractDTO {
    private String roleId;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色描述
     */
    private String description;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RoleDTO{");
        sb.append("roleId='").append(roleId).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
