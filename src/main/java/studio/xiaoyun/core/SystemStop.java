package studio.xiaoyun.core;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 系统停止时执行一些操作
 */
@Component
public class SystemStop implements ApplicationListener<ContextClosedEvent> {
    private Logger log = LoggerFactory.getLogger(SystemStop.class);
    @Resource
    private CacheManager cacheManager;
    private boolean isStop = false;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (isStop) return;   //防止执行多次
        //关闭缓存管理器
        if(cacheManager instanceof Destroyable){
            try {
                ((Destroyable)cacheManager).destroy();
            } catch (Exception e) {
                log.error("关闭cacheManager时出错",e);
            }
        }
        log.info("系统已停止");
        isStop = true;
    }

}
