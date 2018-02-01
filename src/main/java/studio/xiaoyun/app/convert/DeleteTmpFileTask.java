package studio.xiaoyun.app.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import studio.xiaoyun.config.SystemConfig;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * 定时删除临时文件
 */
@Component
public class DeleteTmpFileTask {
    private Logger logger = LoggerFactory.getLogger(DeleteTmpFileTask.class);
    @Resource
    private SystemConfig config;

    /**
     * 删除临时文件，每天凌晨3点定时执行
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteTmpFile(){
        logger.info("开始删除临时文件");
        try{
            long fileAmount;
            Path uploadPath = config.getFilePath().resolve(config.getUploadPath());
            Stream<Path> paths = Files.list(uploadPath);
            fileAmount = paths.count();
            paths = Files.list(uploadPath);
            paths.forEach(this::deletePath);

            Path convertPath = config.getFilePath().resolve(config.getConvertPath());
            paths = Files.list(convertPath);
            fileAmount += paths.count();
            paths = Files.list(convertPath);
            paths.forEach(this::deletePath);
            if(logger.isInfoEnabled()){
                logger.info("删除临时文件完成,共删除了"+fileAmount+"个文件");
            }
        }catch (Exception e){
            logger.error("删除临时文件出错,"+e.getMessage(),e);
        }
    }

    private void deletePath(Path path){
        try{
            if(Files.isDirectory(path)){
                Stream<Path> paths = Files.list(path);
                paths.forEach(this::deletePath);
                Files.delete(path);
            }else{
                Files.delete(path);
            }
        }catch (IOException e){
            logger.error("删除临时文件出错,"+e.getMessage()+",文件名:"+path.toString(),e);
        }
    }
}
