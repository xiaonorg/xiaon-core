package studio.xiaoyun.config;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import studio.xiaoyun.security.auth.AuthRealm;

import java.util.HashMap;
import java.util.Map;

/**
 * shiro相关的配置信息
 */
@Configuration
public class ShiroConfig {

    @Bean(name = "cacheManager")
    public CacheManager getCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:config/ehcache.xml");
        return em;
    }


    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getSecurityManager(DefaultWebSessionManager sessionManager,AuthRealm authRealm) {
        //记录用户登陆状态的cookie
        SimpleCookie rememberMeCookie = new SimpleCookie();
        rememberMeCookie.setName("rme");
        rememberMeCookie.setHttpOnly(true);
        //cookie的有效期，单位是秒，设置为一个月
        rememberMeCookie.setMaxAge(30*24*60*60);
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(rememberMeCookie);

        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(authRealm);
        dwsm.setRememberMeManager(rememberMeManager);
        dwsm.setSessionManager(sessionManager);
        return dwsm;
    }

    @Bean("sessionManager")
    public DefaultWebSessionManager getSessionManager(CacheManager cacheManager){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //session超超时时间，单位是毫秒，设置为半小时
        sessionManager.setGlobalSessionTimeout(30*60*1000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);

        //记录session id的cookie
        SimpleCookie sessionIdCookie = new SimpleCookie();
        sessionIdCookie.setName("sid");
        sessionIdCookie.setHttpOnly(true);
        //cookie的有效期，-1表示当浏览器关闭时自动删除
        sessionIdCookie.setMaxAge(-1);

        //定时删除过期的session
        ExecutorServiceSessionValidationScheduler svs = new ExecutorServiceSessionValidationScheduler();
        //间隔时间，单位时毫秒
        svs.setInterval(30*60*1000);
        svs.setThreadNamePrefix("sessionValidation");
        svs.setSessionManager(sessionManager);

        //实现session的增、删、改、查功能
        EnterpriseCacheSessionDAO sessionDao = new EnterpriseCacheSessionDAO();
        sessionDao.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
        //缓存名称，在ehcache.xml中配置
        sessionDao.setActiveSessionsCacheName("session");
        sessionDao.setCacheManager(cacheManager);

        sessionManager.setSessionIdCookie(sessionIdCookie);
        sessionManager.setSessionValidationScheduler(svs);
        sessionManager.setSessionDAO(sessionDao);
        return sessionManager;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilter(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterMap = new HashMap<>();
        //设置druid下的路径只有管理员才能访问
        filterMap.put("/druid/**", "roles[admin]");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

}
