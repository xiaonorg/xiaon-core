package studio.xiaoyun.security.auth;

import studio.xiaoyun.core.constant.UserType;

/**
 * 保存用户的登陆信息
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {
    private UserType type;

    public UsernamePasswordToken(String name,String password){
        super(name,password);
    }

    /**
     *
     * @return 用户的类型
     */
    public UserType getType() {
        return type;
    }

    /**
     *
     * @param type 用户的类型
     */
    public void setType(UserType type) {
        this.type = type;
    }
}
