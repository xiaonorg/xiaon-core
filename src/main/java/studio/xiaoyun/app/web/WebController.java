package studio.xiaoyun.app.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * xiaoyun-web相关功能的控制器
 */
@RestController
@RequestMapping("/v1/app")
public class WebController {
    private Logger logger = LoggerFactory.getLogger(WebController.class);
    @Resource
    private HomeUpdateHandler updateHandler;

    /**
     * 更新首页
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public void updateHome() {
        updateHandler.updateHome();
    }

    /**
     * @return 获得首页的最后更新时间, 如果无法获得，则返回0
     */
    @RequestMapping(value = "/home/lastUpdateTime", method = RequestMethod.GET)
    public String getLastUpdateTime() {
        Path path = updateHandler.getHomePath();
        if (Files.exists(path)) {
            try {
                Instant instant = Files.getLastModifiedTime(path).toInstant();
                LocalDateTime time = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (IOException e) {
                logger.warn("获得首页更新时间时出错", e);
            }
        }
        return "0";
    }

}
