package studio.xiaoyun.core.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

/**
 * 角色
 */
@Entity
@Table(name = "xy_role")
public class RoleDO {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "studio.xiaoyun.core.pojo.UuidIdentifierGenerator")
    @Column(length = 32,name="roleid")
    private String roleId;

    /**
     * 角色名称
     */
    @Column(unique=true,length = 50, nullable = false)
    private String name;

    /**
     * 角色的描述
     */
    @Column
    private String description;

    @ManyToMany(targetEntity=PermissionDO.class)
    @JoinTable(name = "xy_role_permission",
            joinColumns = @JoinColumn(name = "roleid"),
            inverseJoinColumns = @JoinColumn(name = "permissionid"))
    private Set<PermissionDO> permissions;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<PermissionDO> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionDO> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RoleDO{");
        sb.append("roleId='").append(roleId).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
