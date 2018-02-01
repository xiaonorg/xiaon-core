package studio.xiaoyun.core.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import studio.xiaoyun.core.constant.*;
import studio.xiaoyun.core.pojo.PermissionDO;
import studio.xiaoyun.core.pojo.RoleDO;
import studio.xiaoyun.core.pojo.UserDO;
import studio.xiaoyun.core.query.PermissionQuery;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 系统启动时初始化数据库
 */
@Repository
public class InitDatabase {
    private Logger logger = LoggerFactory.getLogger(InitDatabase.class);
    @Resource
    private SessionUtil sessionUtil;
    @Resource
    private UserDao userDao;
    @Resource
    private RoleDao roleDao;
    @Resource
    private PermissionDao permissionDao;
    @Resource
    private Environment environment;

    @Transactional
    public void initDatabase() {
        logger.debug("开始初始化数据库");
        try{
            initUser();
            initRole();
            initPermission();
            initUserRole();
            initAdminPermission();
        }catch(Exception e){
            logger.error("初始化数据库时出现错误",e);
        }
    }

    private void initUser() throws Exception {
        List<String> list = new ArrayList<>();
        for (SystemUser systemUser : SystemUser.values()) {
            list.add(systemUser.getUserId());
        }
        String hql = "SELECT userId FROM UserDO WHERE userId IN (:userId)";
        Session session = sessionUtil.getSession();
        Query query = session.createQuery(hql);
        query.setParameterList("userId", list);
        //查询出数据库中已经存在的用户id
        @SuppressWarnings("unchecked")
        List<String> list2 = query.list();
        Map<String,String> userIdMap = new HashMap<>();
        for (SystemUser systemUser : SystemUser.values()) {
            if (list2.contains(systemUser.getUserId())) {
                continue;
            }
            String email = environment.getProperty("xiaoyun.user."+systemUser.getName()+".email",systemUser.getName()+"@xiaoyun.studio");
            String password = environment.getProperty("xiaoyun.user."+systemUser.getName()+".password","1");
            UserDO user = new UserDO();
            user.setName(systemUser.getName());
            user.setEmail(email);
            user.setPassword(password);
            user.setStatus(UserStatus.NORMAL);
            user.setUserType(UserType.SYSTEM);
            user.setCreateDate(LocalDateTime.now());
            userDao.save(user);
            userIdMap.put(systemUser.getUserId(),user.getUserId());
        }
        //将自动生成的id改为预定义的id
        if(!userIdMap.isEmpty()){
            session.flush();
            session.clear();
            userIdMap.forEach((key,value)->{
                String updateSql = "update UserDO set userId= :newId where userId= :oldId";
                Query updateQuery = session.createQuery(updateSql);
                updateQuery.setParameter("newId",key);
                updateQuery.setParameter("oldId",value);
                updateQuery.executeUpdate();
            });
        }
        logger.debug("初始化系统用户完成");
    }

    private void initRole() {
        List<String> list = new ArrayList<>();
        for (SystemRole systemRole : SystemRole.values()) {
            list.add(systemRole.getRoleId());
        }
        String hql = "SELECT roleId FROM RoleDO WHERE roleId IN (:roleId)";
        Session session = sessionUtil.getSession();
        Query query = session.createQuery(hql);
        query.setParameterList("roleId", list);
        @SuppressWarnings("unchecked")
        List<String> list2 = query.list();
        Map<String,String> roleIdMap = new HashMap<>();
        for (SystemRole systemRole : SystemRole.values()) {
            if (list2.contains(systemRole.getRoleId())) {
                continue;
            }
            RoleDO role = new RoleDO();
            role.setName(systemRole.getName());
            role.setDescription(systemRole.getDescription());
            roleDao.save(role);
            roleIdMap.put(systemRole.getRoleId(),role.getRoleId());
        }
        //将自动生成的id改为预定义的id
        if(!roleIdMap.isEmpty()){
            session.flush();
            session.clear();
            roleIdMap.forEach((key,value)->{
                String updateSql = "update RoleDO set roleId=:newId where roleId=:oldId";
                Query updateQuery = session.createQuery(updateSql);
                updateQuery.setParameter("newId",key);
                updateQuery.setParameter("oldId",value);
                updateQuery.executeUpdate();
            });
        }
        logger.debug("初始化系统角色完成");
    }

    private void initPermission(){
        String hql = "select name from PermissionDO where name in (:name)";
        Session session = sessionUtil.getSession();
        Query query = session.createQuery(hql);
        query.setParameterList("name", Arrays.asList(Permission.values()));
        @SuppressWarnings("unchecked")
        List<Permission> list2 = query.list();
        for(Permission permission:Permission.values()){
            if(list2.contains(permission)) {
                continue;
            }
            PermissionDO entity = new PermissionDO();
            entity.setName(permission);
            entity.setType(permission.getType());
            entity.setDescription(permission.getDescription());
            permissionDao.save(entity);
        }
        if(list2.size()!=Permission.values().length){
            session.flush();
            session.clear();
        }
        logger.debug("初始化系统权限完成");
    }

    /**
     * 初始化系统用户的角色
     */
    private void initUserRole(){
        //设置admin的角色
        UserDO user = userDao.getById(SystemUser.ADMIN.getUserId());
        Set<RoleDO> userRole = user.getRoles();
        if(userRole.stream().noneMatch(item->item.getRoleId().equals(SystemRole.ADMIN.getRoleId()))){
            RoleDO role = roleDao.loadById(SystemRole.ADMIN.getRoleId());
            userRole.add(role);
            userDao.save(user);
        }
        //设置user的角色
        user = userDao.getById(SystemUser.USER.getUserId());
        userRole = user.getRoles();
        if(userRole.stream().noneMatch(item->item.getRoleId().equals(SystemRole.USER.getRoleId()))){
            RoleDO role = roleDao.loadById(SystemRole.USER.getRoleId());
            userRole.add(role);
            userDao.save(user);
        }
        //设置public的角色
        user = userDao.getById(SystemUser.PUBLIC.getUserId());
        userRole = user.getRoles();
        if(userRole.stream().noneMatch(item->item.getRoleId().equals(SystemRole.PUBLIC.getRoleId()))){
            RoleDO role = roleDao.loadById(SystemRole.PUBLIC.getRoleId());
            userRole.add(role);
            userDao.save(user);
        }
        logger.debug("初始化用户角色完成");
    }

    /**
     * 初始化管理员的权限
     */
    private void initAdminPermission(){
        RoleDO role = roleDao.getById(SystemRole.ADMIN.getRoleId());
        Set<PermissionDO> permissionSet = role.getPermissions();
        PermissionQuery params = new PermissionQuery();
        params.setMaxResults(1000);
        List<PermissionDO> permissionList = permissionDao.listPermissionByParameter(params);
        boolean isUpdate = false;
        for(PermissionDO permission:permissionList){
            if(!permissionSet.contains(permission)){
                permissionSet.add(permission);
                isUpdate = true;
            }
        }
        if(isUpdate){
            roleDao.update(role);
        }
        logger.debug("初始化管理员权限完成");
    }

}
