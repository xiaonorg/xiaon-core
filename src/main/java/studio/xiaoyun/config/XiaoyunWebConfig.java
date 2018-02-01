package studio.xiaoyun.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * xiaoyun-web项目相关的配置
 */
@Configuration
public class XiaoyunWebConfig {
    private Logger logger = LoggerFactory.getLogger(XiaoyunWebConfig.class);

    /**
     * 访问xiaoyun-web项目的url
     */
    private String xiaoyunWebUrl;

    /**
     * 是否更新首页
     */
    private boolean updateHomeEnabled;

    @Resource
    private Environment env;

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        xiaoyunWebUrl = env.getProperty("xiaoyun.web.url", "");
        updateHomeEnabled = Boolean.valueOf(env.getProperty("xiaoyun.web.updateHomeEnabled", "false"));
        if (updateHomeEnabled && StringUtils.isBlank(xiaoyunWebUrl)) {
            logger.error("配置错误，请设置xiaoyun.web.url的值");
            updateHomeEnabled = false;
        }
    }

    public String getXiaoyunWebUrl() {
        return xiaoyunWebUrl;
    }

    public boolean isUpdateHomeEnabled() {
        return updateHomeEnabled;
    }

}
