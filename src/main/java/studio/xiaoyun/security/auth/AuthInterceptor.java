package studio.xiaoyun.security.auth;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import studio.xiaoyun.security.annotation.RequirePermission;
import studio.xiaoyun.core.constant.SystemRole;
import studio.xiaoyun.core.dao.PermissionDao;
import studio.xiaoyun.core.dao.RoleDao;
import studio.xiaoyun.core.pojo.PermissionDO;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 该拦截器会拦截有{@linkplain studio.xiaoyun.security.annotation.RequirePermission RequirePermission}注解的方法，
 * 判断用户是否有执行方法的权限
 */
@Aspect
@Component
public class AuthInterceptor {
    @Resource
    private RoleDao roleDao;
    @Resource
    private PermissionDao permissionDao;
    /**
     * 保存公共的权限
     */
    private List<String> publicPermission;

    @Before("@annotation(studio.xiaoyun.security.annotation.RequirePermission)")
    public void doRequirePermission(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequirePermission requirePermission = signature.getMethod().getDeclaredAnnotation(RequirePermission.class);
        String permission = requirePermission.value().name();
        //获得当前用户
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated() || currentUser.isRemembered()) { //用户已经登陆系统
            if (!currentUser.hasRole(SystemRole.ADMIN.name())) {  //系统管理员拥有所有权限
                currentUser.checkPermission(permission);
            }
        } else {
            List<String> list = getPublicPermission();
            //如果用户是游客身份，并且游客角色没有该权限，则抛出异常
            if (!list.contains(permission)) {
                throw new AuthorizationException("无权限");
            }
        }
    }

    @Before("@annotation(studio.xiaoyun.security.annotation.RequireUser)")
    public void doRequireUser() {
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated() && !currentUser.isRemembered()) {
            throw new AuthorizationException("无权限");
        }
    }

    @Before("@annotation(studio.xiaoyun.security.annotation.RequireGuest)")
    public void doRequireGuest() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated() || currentUser.isRemembered()) {
            throw new AuthorizationException("无权限");
        }
    }

    private List<String> getPublicPermission() {
        if (publicPermission == null || publicPermission.isEmpty()) {
            updatePublicPermission();
        }
        return publicPermission;
    }

    /**
     * 更新缓存数据
     */
    public void updatePublicPermission(){
        List<PermissionDO> permissions = permissionDao.listPermissionByRoleId(SystemRole.PUBLIC.getRoleId(), null);
        publicPermission = permissions.stream().map(item -> item.getName().name()).collect(Collectors.toList());
    }
}
