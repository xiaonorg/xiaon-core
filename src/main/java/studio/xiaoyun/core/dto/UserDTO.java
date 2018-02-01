package studio.xiaoyun.core.dto;

import studio.xiaoyun.core.constant.UserStatus;
import studio.xiaoyun.core.constant.UserType;

import java.time.LocalDateTime;

public class UserDTO implements AbstractDTO {
    private String userId;

    /**
     * 用户名
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态
     */
    private UserStatus status;

    /**
     * 用户类型
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDTO{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", createDate=").append(createDate);
        sb.append(", email='").append(email).append('\'');
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
