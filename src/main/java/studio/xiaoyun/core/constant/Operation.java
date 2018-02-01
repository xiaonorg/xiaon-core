package studio.xiaoyun.core.constant;

/**
 * 定义操作。
 * <p>除了在本类中定义操作外，在{@linkplain Permission Permission}中也有定义</p>
 */
public enum Operation {
    /**
     * 用户登陆
     */
    USER_LOGIN(OperationType.USER,"用户登陆"),
    /**
     * 用户注销
     */
    USER_LOGOUT(OperationType.USER,"用户注销");

    private String description;
    private OperationType type;
    Operation(OperationType type,String description){
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
