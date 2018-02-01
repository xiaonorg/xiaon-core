package studio.xiaoyun.core.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.xiaoyun.core.constant.SystemRole;
import studio.xiaoyun.core.dao.PermissionDao;
import studio.xiaoyun.core.dao.RoleDao;
import studio.xiaoyun.core.pojo.PermissionDO;
import studio.xiaoyun.core.pojo.RoleDO;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.query.PermissionQuery;
import studio.xiaoyun.core.query.RoleQuery;
import studio.xiaoyun.core.query.criterion.Query;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色相关
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {
    private Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    @Resource
    private RoleDao roleDao;
    @Resource
    private PermissionDao permissionDao;

    @Override
    public void setRolePermission(String roleId, List<String> permissionIds) {
        logger.debug("修改角色的权限，角色id:{}",roleId);
        RoleDO role = roleDao.getById(roleId);
        Set<PermissionDO> permissionSet;
        if(permissionIds==null || permissionIds.isEmpty()){
            permissionSet = Collections.emptySet();
        }else{
            PermissionQuery params = new PermissionQuery();
            params.addQuery(Query.in("permissionId",permissionIds));
            List<PermissionDO> permissionList = permissionDao.listPermissionByParameter(params);
            if(permissionIds.size()!=permissionList.size()){
                throw new InvalidParameterException("权限id不存在");
            }
            permissionSet = new HashSet<>(permissionList);
        }
        role.setPermissions(permissionSet);
        roleDao.update(role);
    }

    @Override
    public void deleteRole(List<String> roleId) {
        logger.debug("删除角色，角色id:{}",roleId.toString());
        for(SystemRole systemRole:SystemRole.values()){
            if(roleId.contains(systemRole.getRoleId())){
                throw new InvalidParameterException("系统角色不能删除");
            }
        }
        for(String id:roleId){
            RoleDO role = roleDao.getById(id);
            roleDao.delete(role);
        }
    }

    @Override
    public void updateRole(RoleDO role){
        logger.debug("更新角色信息，角色id:{}，名称:{},描述:{}",role.getRoleId(),role.getName(),role.getDescription());
        if(StringUtils.isBlank(role.getRoleId())){
            throw new InvalidParameterException("角色id不能为空");
        }
        if(StringUtils.isBlank(role.getName())){
            throw new InvalidParameterException("角色名称不能为空");
        }
        for(SystemRole systemRole:SystemRole.values()){
            if(systemRole.getRoleId().equals(role.getRoleId()) && !systemRole.getName().equals(role.getName())){
                throw new InvalidParameterException("系统角色的名称不能修改");
            }
        }
        RoleDO roleDO = roleDao.getById(role.getRoleId());
        if(!roleDO.getName().equals(role.getName())){  //如果修改角色名称，则判断名称是否已经存在
            RoleQuery param = new RoleQuery();
            param.setName(role.getName());
            long count = roleDao.countRoleByParameter(param);
            if(count>0){
                throw new InvalidParameterException("角色名称已经存在");
            }
        }
        roleDO.setName(role.getName());
        roleDO.setDescription(role.getDescription());
        roleDao.update(roleDO);
    }

    @Override
    public String saveRole(String name, String description) {
        logger.debug("创建角色，名称:{},描述:{}",name,description);
        if(StringUtils.isBlank(name)){
            throw new InvalidParameterException("角色名称不能为空");
        }
        RoleQuery param = new RoleQuery();
        param.setName(name);
        long count = roleDao.countRoleByParameter(param);
        if(count>0){
            throw new InvalidParameterException("角色名称已经存在");
        }
        RoleDO role = new RoleDO();
        role.setName(name);
        role.setDescription(description);
        return roleDao.save(role);
    }

}
