package studio.xiaoyun.core.pojo;

import org.hibernate.annotations.GenericGenerator;
import studio.xiaoyun.core.constant.OperationType;
import studio.xiaoyun.core.constant.Permission;

import javax.persistence.*;

/**
 * 权限
 */
@Entity
@Table(name = "xy_permission")
public class PermissionDO {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "studio.xiaoyun.core.pojo.UuidIdentifierGenerator")
    @Column(length = 32,name="permissionid")
    private String permissionId;
    /**
     * 权限名称
     */
    @Column(unique=true,nullable = false)
    @Enumerated(EnumType.STRING)
    private Permission name;
    /**
     * 权限类型
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OperationType type;
    /**
     * 描述
     */
    @Column(length = 200,nullable = false)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionDO that = (PermissionDO) o;
        return permissionId.equals(that.permissionId);
    }

    @Override
    public int hashCode() {
        return permissionId.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PermissionDO{");
        sb.append("permissionId='").append(permissionId).append('\'');
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
