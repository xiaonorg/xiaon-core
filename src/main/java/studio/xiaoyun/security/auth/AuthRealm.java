package studio.xiaoyun.security.auth;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;
import studio.xiaoyun.core.dao.PermissionDao;
import studio.xiaoyun.core.dao.RoleDao;
import studio.xiaoyun.core.dao.UserDao;
import studio.xiaoyun.core.pojo.PermissionDO;
import studio.xiaoyun.core.pojo.RoleDO;
import studio.xiaoyun.core.pojo.UserDO;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class AuthRealm extends AuthorizingRealm {
    @Resource
    private UserDao userDao;
    @Resource
    private RoleDao roleDao;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private CacheManager cacheManager;
    @Resource
    private CredentialsMatcher credentialsMatcher;

    @PostConstruct
    public void initPara(){
        setCredentialsMatcher(credentialsMatcher);
        setCacheManager(cacheManager);
        setCachingEnabled(true);
        setAuthenticationCachingEnabled(true);
        //保存认证信息的缓存的名称，默认在ehcache.xml中配置
        setAuthenticationCacheName("authentication");
        setAuthorizationCachingEnabled(true);
        //保存授权信息的缓存的名称，默认在ehcache.xml中配置
        setAuthorizationCacheName("authorization");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String userId = (String) pc.fromRealm(this.getName()).iterator().next();
        //获得用户的所有权限
        List<PermissionDO> permissions = permissionDao.listPermissionByUserId(userId, null);
        for (PermissionDO permission : permissions) {
            info.addStringPermission(permission.getName().name());
        }
        //获得用户的所有角色
        List<RoleDO> roles = roleDao.listRoleByUserId(userId, null);
        for (RoleDO role : roles) {
            info.addRole(role.getName());
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        Optional<UserDO> user = userDao.getUserByEmail(token.getUsername());
        if (!user.isPresent()) { //如果根据邮箱无法找到用户，则根据用户名查找
            user = userDao.getUserByName(token.getUsername());
        }
        if (!user.isPresent()) {
            throw new UnknownAccountException();  //用户名或者邮箱不存在
        }
        UserDO userInfo = user.get();
        //检查用户的状态
        switch (userInfo.getStatus()) {
            case NORMAL:
                break;
            case DELETED:
                throw new DisabledAccountException();  //账号不可用
            default:
                throw new AccountException("无法识别的参数:" + userInfo.getStatus());  //内部错误
        }
        return new SimpleAuthenticationInfo(userInfo.getUserId(), userInfo.getPassword(), this.getName());
    }

}
