package studio.xiaoyun.core.constant;

/**
 * 系统预定义的一些角色
 */
public enum SystemRole {
    /**
     * 系统管理员
     */
    ADMIN("2843538428204e8aa3932d4f7df5b35e","admin","系统管理员"),
    /**
     * 包括所有注册用户和管理员
     */
    USER("6727ecb0374346f59566ddb17cd2b2f5","user","注册用户和管理员"),
    /**
     * 当前登陆用户
     */
    CURRENT_USER("b6c5645f06e54b57a54d53c8a310cebe","current_user","当前登陆用户"),
    /**
     * 包括所有用户
     */
    PUBLIC("caa98109ec924a48b47fd8d02e9f4b18","public","所有用户");

    private String roleId;
    private String name;
    private String description;
    SystemRole(String roleId,String name,String description){
        this.roleId = roleId;
        this.name = name;
        this.description = description;
    }

    /**
     *
     * @return 角色的名称
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return 角色的描述
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return 角色的id
     */
    public String getRoleId(){
        return roleId;
    }

}
