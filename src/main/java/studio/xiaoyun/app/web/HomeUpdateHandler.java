package studio.xiaoyun.app.web;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.bus.EventBus;
import reactor.spring.context.annotation.Consumer;
import reactor.spring.context.annotation.Selector;
import studio.xiaoyun.app.convert.tool.FileTool;
import studio.xiaoyun.config.SystemConfig;
import studio.xiaoyun.config.XiaoyunWebConfig;
import studio.xiaoyun.core.exception.XyException;
import studio.xiaoyun.event.event.ArticleDeleteEvent;
import studio.xiaoyun.event.event.ArticleReleaseEvent;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 更新网站首页
 */
@Consumer
@Component
public class HomeUpdateHandler {
    private Logger logger = LoggerFactory.getLogger(HomeUpdateHandler.class);
    @Resource
    private EventBus eventBus;
    @Resource
    private SystemConfig systemConfig;
    @Resource
    private XiaoyunWebConfig webConfig;
    /**
     * 记录最后一次的更新时间，防止更新太频繁
     */
    private AtomicLong lastUpdateTime = new AtomicLong();
    /**
     * 两次更新的最小间隔时间
     */
    private final static long interval = 10*60*1000;

    /**
     *  发布文章
     * @param event 事件
     */
    @Selector(ArticleReleaseEvent.EVENT_NAME)
    public void articleRelease(ArticleReleaseEvent event) {
        try{
            updateHome(true);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    /**
     * 删除文章
     * @param event 事件
     */
    @Selector(ArticleDeleteEvent.EVENT_NAME)
    public void articleDelete(ArticleDeleteEvent event) {
        try{
            updateHome(true);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    /**
     * 更新首页
     * @param isInterval 是否考虑间隔时间
     */
    private void updateHome(boolean isInterval){
        if(!webConfig.isUpdateHomeEnabled()){
            return;
        }
        logger.debug("开始更新首页");
        long date = new Date().getTime();
        if (!isInterval || date - lastUpdateTime.longValue() > interval) {
            lastUpdateTime.set(date);
            try{
                String text = getText(webConfig.getXiaoyunWebUrl());
                Path path = getHomePath();
                if(Files.notExists(path.getParent())){
                    Files.createDirectories(path.getParent());
                }
                if(Files.exists(path)){
                    Files.delete(path);
                }
                FileTool.writeFile(path,text);
            }catch (IOException e){
                throw new XyException("更新首页失败,"+e.getMessage(),e);
            }
        }
    }

    /**
     * 更新首页
     */
    public void updateHome() {
        updateHome(false);
    }

    /**
     *
     * @return 首页文件的路径
     */
    public Path getHomePath(){
        return systemConfig.getFilePath().resolve(systemConfig.getArticlePath()).resolve("home").resolve("index.html");
    }

    /**
     * 根据url获得数据
     * @param url url
     * @return url的数据
     */
    String getText(String url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() < 400) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, "utf-8");
            }else{
                throw new XyException("获取网页数据失败,状态码:"+response.getStatusLine().getStatusCode());
            }
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
    }
}
