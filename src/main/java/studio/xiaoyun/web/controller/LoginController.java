package studio.xiaoyun.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import studio.xiaoyun.core.constant.UserType;
import studio.xiaoyun.core.dao.UserDao;
import studio.xiaoyun.core.dto.UserDTO;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.pojo.UserDO;
import studio.xiaoyun.security.auth.UsernamePasswordToken;
import studio.xiaoyun.web.ErrorCode;
import studio.xiaoyun.web.WebException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登陆、注销
 */
@RestController
@RequestMapping("/v1")
public class LoginController {
    private Logger log = LoggerFactory.getLogger(LoginController.class);
    @Resource
    private UserDao userDao;


    /**
     * 用户登陆
     * @param request 请求
     * @param name 用户名
     * @param password 密码
     * @param rememberMe 是否记住密码，一段时间内不用再登陆
     * @return 用户信息
     */
    @RequestMapping(value = "/user/login",method = RequestMethod.POST)
    public UserDTO userLogin(HttpServletRequest request, String name, String password, boolean rememberMe) {
        log.debug("用户登陆开始,name:{},rememberMe:{},ip:{}",name,rememberMe,request.getRemoteAddr());
        if(StringUtils.isBlank(name)){
            throw new InvalidParameterException("用户名不能为空!");
        }
        if(StringUtils.isBlank(password)){
            throw new InvalidParameterException("密码不能为空!");
        }
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);
        token.setRememberMe(rememberMe);
        token.setType(UserType.USER);
        token.setHost(request.getRemoteHost());
        UserDTO userDTO = login(token);
        log.debug("用户登陆成功,name:{}",name);
        return userDTO;
    }

    /**
     * 用户注销
     */
    @RequestMapping(value = "/user/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(){
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() || subject.isRemembered()) {
            log.debug("用户注销,userId="+subject.getPrincipal().toString());
            subject.logout();
        }
    }

    private UserDTO login(UsernamePasswordToken token){
        UserDTO userDTO = new UserDTO();
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            try {
                subject.login(token);
            } catch (UnknownAccountException uae) {  //用户名不存在
                throw new WebException("用户名不存在",ErrorCode.UNKNOWN_ACCOUNT);
            } catch (IncorrectCredentialsException ice) {  //密码错误
                throw new WebException("密码错误",ErrorCode.INCORRECT_PASSWORD);
            } catch (DisabledAccountException lae) {  //账户不可用、已删除
                throw new WebException("账户不可用",ErrorCode.DISABLED_ACCOUNT);
            }
        }
        //登陆成功后返回用户信息到客户端
        String userId = subject.getPrincipal().toString();
        UserDO userInfo = userDao.getById(userId);
        userDTO.setUserId(userInfo.getUserId());
        userDTO.setName(userInfo.getName());
        return userDTO;
    }

    /**
     *
     * @return 获得当前登陆用户的信息,如果用户没有登录，则返回noLogin
     */
    @RequestMapping(value = "/user/me",method = RequestMethod.GET)
    public Map<String,Object> getCurrentUserInfo(HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        Map<String,Object> map = new HashMap<>();
        if (subject.isAuthenticated() || subject.isRemembered()) {   //如果用户已经登录
            String userId = subject.getPrincipal().toString();
            UserDO userInfo = userDao.getById(userId);
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userInfo.getUserId());
            userDTO.setName(userInfo.getName());
            map.put("result",userDTO);
        }else{
            map.put("result","noLogin");
        }
        return map;
    }

}
