package studio.xiaoyun.core.query;

import studio.xiaoyun.core.constant.OperationType;
import studio.xiaoyun.core.constant.Permission;

public class PermissionQuery extends AbstractQuery {
    /**
     * 权限Id
     */
    private String permissionId;
    /**
     * 权限名称
     */
    private Permission name;
    /**
     * 权限的类型
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

}
