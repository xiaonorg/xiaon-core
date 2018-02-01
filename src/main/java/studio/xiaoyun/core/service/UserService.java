package studio.xiaoyun.core.service;

import studio.xiaoyun.core.dto.UserDTO;

import java.util.List;

/**
 * 用户业务类
 */
public interface UserService {
    /**
     * 创建用户
     *
     * @param name     用户名
     * @param email    邮箱
     * @param password 密码
     * @return 用户id
     */
    String createUser(String name, String email, String password);

    /**
     * 更新用户信息
     *
     * @param userId 用户id
     * @param name   用户名
     * @param email  邮箱
     */
    void updateUser(String userId, String name, String email);

    /**
     * 删除用户
     *
     * @param userId 用户id
     */
    void deleteUser(String userId);

    /**
     * 修改密码
     *
     * @param userId   用户id
     * @param password 密码
     */
    void updateUserPassword(String userId, String password);

    /**
     * 设置用户拥有的角色
     *
     * @param userId  用户id
     * @param roleIds 角色id
     */
    void setUserRole(String userId, List<String> roleIds);
}
