package studio.xiaoyun.core.query;

import studio.xiaoyun.core.constant.UserStatus;
import studio.xiaoyun.core.constant.UserType;
import studio.xiaoyun.core.query.criterion.AbstractCriterion;
import studio.xiaoyun.core.query.criterion.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户搜索类
 */
public class UserQuery extends AbstractQuery {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户的名称
     */
    private String name;
    /**
     * 注册时间
     */
    private LocalDateTime createDate;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 状态
     */
    private UserStatus status;
    /**
     * 类型
     */
    private UserType userType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

}
