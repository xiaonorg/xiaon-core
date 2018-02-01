package studio.xiaoyun.web.controller;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import studio.xiaoyun.core.constant.Permission;
import studio.xiaoyun.core.dao.PermissionDao;
import studio.xiaoyun.core.dao.RoleDao;
import studio.xiaoyun.core.pojo.PermissionDO;
import studio.xiaoyun.core.pojo.RoleDO;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.query.PermissionQuery;
import studio.xiaoyun.core.query.RoleQuery;
import studio.xiaoyun.core.service.RoleService;
import studio.xiaoyun.security.annotation.RequirePermission;
import studio.xiaoyun.web.ParameterUtil;
import studio.xiaoyun.web.WebResult;
import studio.xiaoyun.core.dto.PermissionDTO;
import studio.xiaoyun.core.dto.DTOConverter;
import studio.xiaoyun.core.dto.RoleDTO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * 角色相关
 */
@RestController
@RequestMapping("/v1/role")
public class RoleController {
    @Resource
    private RoleDao roleDao;
    @Resource
    private DTOConverter resourceUtil;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private RoleService roleService;

    /**
     * 创建角色
     * @param name 角色名称
     * @param description 角色描述
     * @return 角色信息
     */
    @RequirePermission(Permission.ROLE_CREATE)
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public RoleDTO saveRole(String name, String description){
        String roleId = roleService.saveRole(name,description);
        RoleDO roleDO = roleDao.getById(roleId);
        RoleDTO resources = resourceUtil.toDto(roleDO,null,RoleDTO.class);
        return resources;
    }

    /**
     * 更新角色基本信息
     * @param roleId 角色id
     * @param name 角色名称
     * @param description 角色描述
     */
    @RequirePermission(Permission.ROLE_UPDATE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id:\\S{32}}", method = RequestMethod.POST)
    public void updateRole(@PathVariable("id") String roleId, String name,String description){
        RoleDO role = new RoleDO();
        role.setRoleId(roleId);
        role.setName(name);
        role.setDescription(description);
        roleService.updateRole(role);
    }

    /**
     * 批量删除角色
     * @param roleId 角色id，json格式的列表，例如：["aaaadd","bbbbb"]
     */
    @RequirePermission(Permission.ROLE_DELETE_ALL)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public void deleteRole(String roleId){
        if(StringUtils.isBlank((roleId))){
            throw new InvalidParameterException("roleId参数不能为空");
        }
        List<String> list = JSON.parseArray(roleId,String.class);
        roleService.deleteRole(list);
    }

    /**
     * 获得所有角色
     * @param request http请求
     * @return 角色信息
     */
    @RequirePermission(Permission.ROLE_GET_ALL)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public WebResult getRoleByParameter(HttpServletRequest request) {
        RoleQuery param = ParameterUtil.getParameter(request, RoleQuery.class);
        long count = roleDao.countRoleByParameter(param);
        List<RoleDO> list = roleDao.listRoleByParameter(param);
        List<RoleDTO> resources = resourceUtil.toDto(list, param, RoleDTO.class);
        return new WebResult(count, resources);
    }

    /**
     * 获得角色拥有的权限
     * @param request http请求
     * @return 权限列表
     */
    @RequirePermission(Permission.PERMISSION_GET_BY_ROLEID)
    @RequestMapping(value = "/{id:\\S{32}}/permission", method = RequestMethod.GET)
    public WebResult getPermissionByRoleId(HttpServletRequest request, @PathVariable("id") String roleId) {
        PermissionQuery param = ParameterUtil.getParameter(request, PermissionQuery.class);
        long count = permissionDao.countPermissionByRoleId(roleId, param);
        List<PermissionDO> list = permissionDao.listPermissionByRoleId(roleId, param);
        List<PermissionDTO> resources = resourceUtil.toDto(list, param, PermissionDTO.class);
        return new WebResult(count, resources);
    }

    /**
     * 设置角色拥有的权限
     * @param permissionId 权限id，json格式的列表,如果为空，则表示删除角色拥有的所有权限
     */
    @RequirePermission(Permission.ROLE_UPDATE_PERMISSION)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id:\\S{32}}/permission", method = RequestMethod.POST)
    public void setRolePermission(@PathVariable("id") String roleId, String permissionId) {
        List<String> list = null;
        if(StringUtils.isNotBlank(permissionId)){
            list = JSON.parseArray(permissionId,String.class);
        }
        roleService.setRolePermission(roleId,list);
    }

}
