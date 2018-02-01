package studio.xiaoyun.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import studio.xiaoyun.core.dao.InitDatabase;

import javax.annotation.Resource;

/**
 * 执行系统初始化工作
 */
@Component
public class SystemInit implements ApplicationListener<ContextRefreshedEvent> {
    private Logger log = LoggerFactory.getLogger(SystemInit.class);
    @Resource
    private InitDatabase initDatabase;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() != null) {
            return;
        }
        try {
            initDatabase.initDatabase(); //初始化数据库
            log.info("系统启动!");
        } catch (Exception e) {
            log.error("系统启动时出现错误", e);
        }
    }

}
