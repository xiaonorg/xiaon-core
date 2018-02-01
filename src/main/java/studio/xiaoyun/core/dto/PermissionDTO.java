package studio.xiaoyun.core.dto;

import studio.xiaoyun.core.constant.OperationType;
import studio.xiaoyun.core.constant.Permission;

/**
 * 权限
 */
public class PermissionDTO implements AbstractDTO {
    private String permissionId;
    /**
     * 权限名称
     */
    private Permission name;
    /**
     * 权限类型
     */
    private OperationType type;
    /**
     * 描述
     */
    private String description;

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public Permission getName() {
        return name;
    }

    public void setName(Permission name) {
        this.name = name;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PermissionDTO{");
        sb.append("permissionId='").append(permissionId).append('\'');
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
