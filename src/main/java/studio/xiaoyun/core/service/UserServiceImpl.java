package studio.xiaoyun.core.service;

import org.springframework.stereotype.Service;
import studio.xiaoyun.core.constant.SystemUser;
import studio.xiaoyun.core.constant.UserStatus;
import studio.xiaoyun.core.constant.UserType;
import studio.xiaoyun.core.dao.RoleDao;
import studio.xiaoyun.core.dao.UserDao;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.pojo.RoleDO;
import studio.xiaoyun.core.pojo.UserDO;
import studio.xiaoyun.core.query.RoleQuery;
import studio.xiaoyun.core.query.criterion.Query;
import studio.xiaoyun.security.crypto.CipherService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private RoleDao roleDao;
    @Resource
    private CipherService cipherService;

    @Override
    public String createUser(String name, String email, String password) {
        UserDO user = new UserDO();
        user.setPassword(cipherService.encrypt(password));
        user.setName(name);
        user.setEmail(email);
        user.setCreateDate(LocalDateTime.now());
        user.setStatus(UserStatus.NORMAL);
        user.setUserType(UserType.USER);
        String userID = userDao.save(user);
        return userID;
    }

    @Override
    public void updateUser(String userId, String name, String email) {
        UserDO userDO = userDao.getById(userId);
        userDO.setName(name);
        userDO.setEmail(email);
        userDao.update(userDO);
    }

    @Override
    public void deleteUser(String userId) {
        for (SystemUser user : SystemUser.values()) {
            if (user.getUserId().equals(userId)) {
                throw new InvalidParameterException("不能删除系统用户");
            }
        }
        userDao.delete(userId);
    }

    @Override
    public void updateUserPassword(String userId, String password) {
        UserDO user = userDao.getById(userId);
        user.setPassword(cipherService.encrypt(password));
        userDao.update(user);
    }

    @Override
    public void setUserRole(String userId, List<String> roleIds) {
        UserDO user = userDao.getById(userId);
        Set<RoleDO> roleSet;
        if (roleIds == null || roleIds.isEmpty()) {
            roleSet = Collections.emptySet();
        } else {
            RoleQuery params = new RoleQuery();
            params.addQuery(Query.in("roleId", roleIds));
            List<RoleDO> roleDOList = roleDao.listRoleByParameter(params);
            if (roleIds.size() != roleDOList.size()) {
                throw new InvalidParameterException("角色id不存在");
            } else {
                roleSet = new HashSet<>(roleDOList);
            }
        }
        user.setRoles(roleSet);
        userDao.update(user);
    }
}
