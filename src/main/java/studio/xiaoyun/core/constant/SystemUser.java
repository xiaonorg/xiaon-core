package studio.xiaoyun.core.constant;

/**
 * 系统预定义的一些用户
 */
public enum SystemUser {
    /**
     * 管理员
     */
    ADMIN("499fec455d554a288ef9114308a0f791","admin"),
    /**
     * 注册用户
     */
    USER("88cba39b8e62443da1bf225e91b91ab0","user"),
    /**
     * 公共用户
     */
    PUBLIC("d076851e05dd4e298fc89e75490b81bc","public");

    private String userId;
    private String name;

    SystemUser(String userId,String name){
        this.userId = userId;
        this.name = name;
    }

    /**
     *
     * @return 用户名
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @return 用户Id
     */
    public String getUserId() {
        return userId;
    }
}
