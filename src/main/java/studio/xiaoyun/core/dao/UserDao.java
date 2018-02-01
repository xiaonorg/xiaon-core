package studio.xiaoyun.core.dao;


import studio.xiaoyun.core.pojo.UserDO;
import studio.xiaoyun.core.query.UserQuery;

import java.util.List;
import java.util.Optional;

/**
 * 用户
 */
public interface UserDao extends AbstractDao<UserDO> {

    /**
     * 根据用户名获得用户信息
     * @param name 用户名
     * @return 用户信息
     */
    Optional<UserDO> getUserByName(String name);

    /**
     * 根据邮箱判断用户是否存在
     * @param email 邮箱
     * @return 用户信息
     */
    Optional<UserDO> getUserByEmail(String email);

    /**
     * 获得用户的数量
     * @param parameter 搜索参数
     * @return 用户的数量
     */
    long countUserByParameter(UserQuery parameter);

    /**
     * 搜索用户
     * @param parameter 搜索参数
     * @return 用户列表
     */
    List<UserDO> listUserByParameter(UserQuery parameter);

    /**
     * 根据用户id获得用户的名称
     * @param userIds 用户id
     * @return 用户名称
     */
    List<UserDO> listNameByUserIds(List<String> userIds);

}
