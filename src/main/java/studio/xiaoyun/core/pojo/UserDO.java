package studio.xiaoyun.core.pojo;

import org.hibernate.annotations.GenericGenerator;
import studio.xiaoyun.core.constant.UserStatus;
import studio.xiaoyun.core.constant.UserType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户
 */
@Entity
@Table(name = "xy_user")
public class UserDO {
    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "studio.xiaoyun.core.pojo.UuidIdentifierGenerator")
    @Column(length = 32,name="userid")
    private String userId;

    /**
     * 用户名
     */
    @Column(nullable = false,length = 50)
    private String name;

    /**
     * 登陆密码
     */
    @Column(length = 100, nullable = false)
    private String password;

    /**
     * 创建时间
     */
    @Column(nullable = false,name="createdate")
    private LocalDateTime createDate;

    /**
     * 邮箱
     */
    @Column
    private String email;

    /**
     * 用户状态
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    /**
     * 用户类型
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name="usertype")
    private UserType userType;

    /**
     * 用户拥有的角色
     */
    @ManyToMany(targetEntity=RoleDO.class)
    @JoinTable(name = "xy_user_role",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "roleid"))
    private Set<RoleDO> roles;

    /**
     * 用户拥有的权限
     */
    @ManyToMany(targetEntity=PermissionDO.class)
    @JoinTable(name = "xy_user_permission",
            joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "permissionid"))
    private Set<PermissionDO> permissions;

    public Set<RoleDO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDO> roles) {
        this.roles = roles;
    }

    public Set<PermissionDO> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionDO> permissions) {
        this.permissions = permissions;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDO{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", createDate=").append(createDate);
        sb.append(", email='").append(email).append('\'');
        sb.append(", status=").append(status);
        sb.append(", userType=").append(userType);
        sb.append('}');
        return sb.toString();
    }
}
