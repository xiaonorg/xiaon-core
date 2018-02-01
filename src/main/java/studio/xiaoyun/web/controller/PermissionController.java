package studio.xiaoyun.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import studio.xiaoyun.core.constant.Permission;
import studio.xiaoyun.core.dao.PermissionDao;
import studio.xiaoyun.core.pojo.PermissionDO;
import studio.xiaoyun.core.query.PermissionQuery;
import studio.xiaoyun.security.annotation.RequirePermission;
import studio.xiaoyun.web.ParameterUtil;
import studio.xiaoyun.core.dto.PermissionDTO;
import studio.xiaoyun.core.dto.DTOConverter;
import studio.xiaoyun.web.WebResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/v1/permission")
public class PermissionController {
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private DTOConverter resourceUtil;

    /**
     * 搜索权限
     * @param request http请求
     * @return 权限列表
     */
    @RequirePermission(Permission.PERMISSION_GET_ALL)
    @RequestMapping(value = "",method = RequestMethod.GET)
    public WebResult getPermissionByParameter(HttpServletRequest request){
        PermissionQuery param = ParameterUtil.getParameter(request, PermissionQuery.class);
        long count = permissionDao.countPermissionByParameter(param);
        List<PermissionDO> list = permissionDao.listPermissionByParameter(param);
        List<PermissionDTO> resources = resourceUtil.toDto(list,param,PermissionDTO.class);
        return new WebResult(count,resources);
    }

}
