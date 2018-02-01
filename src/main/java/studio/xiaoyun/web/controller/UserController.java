package studio.xiaoyun.web.controller;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import studio.xiaoyun.core.constant.Permission;
import studio.xiaoyun.core.dao.RoleDao;
import studio.xiaoyun.core.dao.UserDao;
import studio.xiaoyun.core.dto.DTOConverter;
import studio.xiaoyun.core.dto.RoleDTO;
import studio.xiaoyun.core.dto.UserDTO;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.pojo.RoleDO;
import studio.xiaoyun.core.pojo.UserDO;
import studio.xiaoyun.core.query.RoleQuery;
import studio.xiaoyun.core.query.UserQuery;
import studio.xiaoyun.core.service.UserService;
import studio.xiaoyun.security.annotation.RequirePermission;
import studio.xiaoyun.web.ParameterUtil;
import studio.xiaoyun.web.WebResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户相关
 */
@RestController
@RequestMapping("/v1/user")
public class UserController {
    @Resource
    private UserDao userDao;
    @Resource
    private UserService userService;
    @Resource
    private DTOConverter resourceUtil;
    @Resource
    private RoleDao roleDao;

    /**
     * 创建用户
     */
    @RequirePermission(Permission.USER_CREATE)
    @RequestMapping(value = "",method = RequestMethod.PUT)
    public UserDTO saveUser(String name,String email, String password) {
        if(StringUtils.isBlank(email)){
            throw new InvalidParameterException("邮箱不能为空!");
        }
        if(StringUtils.isBlank(password)){
            throw new InvalidParameterException("密码不能为空");
        }
        if(StringUtils.isBlank(name)){
            throw new InvalidParameterException("名称不能为空");
        }
        String userID = userService.createUser(name,email,password);
        UserDO user = userDao.getById(userID);
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setName(user.getName());
        return userDTO;
    }

    /**
     * 更新用户信息
     */
    @RequirePermission(Permission.USER_UPDATE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id:\\S{32}}",method = RequestMethod.POST)
    public void updateUser(@PathVariable("id")String userId, String name, String email){
        if(StringUtils.isBlank(email)){
            throw new InvalidParameterException("邮箱不能为空!");
        }
        if(StringUtils.isBlank(name)){
            throw new InvalidParameterException("名称不能为空");
        }
        userService.updateUser(userId,name,email);
    }

    /**
     * 删除用户
     * @param userId  用户id
     */
    @RequirePermission(Permission.USER_DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id:\\S{32}}",method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id")String userId){
        userService.deleteUser(userId);
    }

    /**
     * 获得所有用户
     */
    @RequirePermission(Permission.USER_GET_ALL)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public WebResult getUserByParameter(HttpServletRequest request) {
        UserQuery param = ParameterUtil.getParameter(request, UserQuery.class);
        long count = userDao.countUserByParameter(param);
        List<UserDO> list = userDao.listUserByParameter(param);
        List<UserDTO> resources = resourceUtil.toDto(list, param, UserDTO.class);
        return new WebResult(count, resources);
    }

    /**
     * 根据用户id获得用户拥有的角色
     */
    @RequirePermission(Permission.ROLE_GET_BY_USERID)
    @RequestMapping(value = "/{id:\\S{32}}/role",method = RequestMethod.GET)
    public WebResult getRoleByUserId(HttpServletRequest request,@PathVariable("id")String userId){
        RoleQuery param = ParameterUtil.getParameter(request,RoleQuery.class);
        long count = roleDao.countRoleByUserId(userId,param);
        List<RoleDO> roleDOList = roleDao.listRoleByUserId(userId,param);
        List<RoleDTO> resources = resourceUtil.toDto(roleDOList,param,RoleDTO.class);
        return new WebResult(count, resources);
    }

    /**
     * 设置用户的角色
     */
    @RequirePermission(Permission.USER_UPDATE_ROLE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id:\\S{32}}/role", method = RequestMethod.POST)
    public void setUserRole(@PathVariable("id") String userId, String roleId) {
        List<String> list = null;
        if(StringUtils.isNotBlank(roleId)){
            list = JSON.parseArray(roleId,String.class);
        }else{
            throw new InvalidParameterException("参数roleId不能为空");
        }
        userService.setUserRole(userId,list);
    }
}
