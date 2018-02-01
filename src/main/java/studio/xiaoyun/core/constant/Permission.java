package studio.xiaoyun.core.constant;

/**
 * 定义所有权限。
 * <p>命名规则：[类型]_[增、删、改、查]_[操作]</p>
 */
public enum Permission {
    /**
     * 添加新用户
     */
    USER_CREATE(OperationType.USER, "添加新用户"),
    /**
     * 更新用户信息
     */
    USER_UPDATE(OperationType.USER, "更新用户信息"),
    /**
     * 更新用户的角色
     */
    USER_UPDATE_ROLE(OperationType.USER, "更新用户的角色"),
    /**
     * 删除用户
     */
    USER_DELETE(OperationType.USER, "删除用户"),
    /**
     * 获得所有用户
     */
    USER_GET_ALL(OperationType.USER, "获得所有用户"),

    /**
     * 创建新角色
     */
    ROLE_CREATE(OperationType.ROLE, "创建新角色"),
    /**
     * 更新角色的基本信息
     */
    ROLE_UPDATE(OperationType.ROLE, "更新角色的基本信息"),
    /**
     * 更新角色的权限
     */
    ROLE_UPDATE_PERMISSION(OperationType.ROLE, "更新角色的权限"),
    /**
     * /**
     * 批量删除角色
     */
    ROLE_DELETE_ALL(OperationType.ROLE, "批量删除角色"),
    /*
     * 获得所有角色
     */
    ROLE_GET_ALL(OperationType.ROLE, "获得所有角色"),
    /**
     * 根据用户id获得用户的角色
     */
    ROLE_GET_BY_USERID(OperationType.ROLE, "根据用户id获得用户的角色"),

    /**
     * 获得所有权限
     */
    PERMISSION_GET_ALL(OperationType.SECURITY, "获得所有权限"),
    /**
     * 根据角色id获得该角色的权限
     */
    PERMISSION_GET_BY_ROLEID(OperationType.SECURITY, "根据角色id获得该角色的权限"),

    /**
     * 获得所有意见反馈
     */
    FEEDBACK_GET_ALL(OperationType.MESSAGE, "获得所有意见反馈"),
    /**
     * 删除所有意见反馈
     */
    FEEDBACK_DELETE_ALL(OperationType.MESSAGE, "批量删除意见反馈");

    private String description;
    private OperationType type;

    Permission(OperationType type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public OperationType getType() {
        return type;
    }
}

