package studio.xiaoyun.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import studio.xiaoyun.core.exception.XyException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;

/**
 * 系统配置信息
 */
@Component
public class SystemConfig {
    /**
     * 应用根目录
     */
    private Path basePath;
    /**
     * 存放文件的目录，可以在配置文件中配置，默认和应用根目录相同
     */
    private Path filePath;
    /**
     * 存放上传文件的临时目录
     */
    private String uploadPath = "file" + File.separator+"public"+File.separator + "upload" + File.separator;
    /**
     * 存放文件格式转换后新文件的目录
     */
    private String convertPath= "file" + File.separator +"public"+File.separator+ "convert" + File.separator;
    /**
     * 存放图片的目录
     */
    private String imagePath = "file" + File.separator +"public"+File.separator+ "img" + File.separator;
    /**
     * 存放文章的目录
     */
    private String articlePath = "file" + File.separator +"article"+File.separator;

    /**
     * 限制可以上传的文件的扩展名
     */
    private Set<String> allowedExtension;
    @Resource
    private Environment environment;
    private Logger logger = LoggerFactory.getLogger(SystemConfig.class);

    /**
     * @return 应用根目录
     */
    public Path getBasePath() {
        return basePath;
    }

    /**
     * @return 存放上传文件的临时目录
     */
    public String getUploadPath() {
        return uploadPath;
    }

    /**
     * @return 存放文件的目录
     */
    public Path getFilePath() {
        return filePath;
    }

    /**
     * @return 存放文件格式转换后新文件的目录
     */
    public String getConvertPath() {
        return convertPath;
    }

    /**
     *
     * @return 存放图片的目录
     */
    public String getImagePath(){
        return imagePath;
    }

    /**
     * @return 限制可以上传的文件的扩展名
     */
    public Set<String> getAllowedExtension() {
        return allowedExtension;
    }

    /**
     *
     * @return 存放文章的目录
     */
    public String getArticlePath(){
        return articlePath;
    }

    @PostConstruct
    public void init() {
        setBasePath();
        String filePathStr = environment.getProperty("xiaoyun.file.path", "");
        if (Paths.get(filePathStr).isAbsolute()) { //判断是否是绝对路径
            filePath = Paths.get(filePathStr);
        } else {
            filePath = basePath.resolve(filePathStr);
        }
        Path uploadPath2 = filePath.resolve(uploadPath);
        Path convertPath2 = filePath.resolve(convertPath);
        Path articlePath2 = filePath.resolve(articlePath);
        Path imagePath2 = filePath.resolve(imagePath);
        try {
            //如果文件夹不存在,则创建目录
            if (Files.notExists(uploadPath2)) {
                Files.createDirectories(uploadPath2);
            }
            if (Files.notExists(convertPath2)) {
                Files.createDirectories(convertPath2);
            }
            if(Files.notExists(articlePath2)){
                Files.createDirectories(articlePath2);
            }
            if(Files.notExists(imagePath2)){
                Files.createDirectories(imagePath2);
            }
        } catch (IOException e) {
            logger.error("创建目录失败," + e.getMessage(), e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("basePath:{}" ,basePath.toString());
            logger.debug("filePath:{}" ,filePath.toString());
        }

        //限制可以上传的文件的扩展名
        String allowedExtensionStr = environment.getProperty("xiaoyun.file.allowedExtension", "");
        String[] allowedExtensionArray = allowedExtensionStr.split(",");
        Set<String> allowedExtensionSet = new HashSet<>();
        for (String allowedExtension : allowedExtensionArray) {
            if (allowedExtension.trim().length() != 0) {
                allowedExtensionSet.add(allowedExtension.trim().toLowerCase());
            }
        }
        allowedExtension = allowedExtensionSet;
    }

    /**
     * 获得系统根目录。
     * <p>参考{@linkplain org.springframework.boot.ApplicationHome ApplicationHome}写的，
     * spring boot就是根据这个类获得系统根目录,但是当路径中包含中文时，这个类可能会出错，所以自己写了一个</p>
     */
    private void setBasePath() {
        CodeSource codeSource = SystemConfig.class.getProtectionDomain().getCodeSource();
        URL location = codeSource == null ? null : codeSource.getLocation();
        File source = null;
        if (location != null) {
            try {
                URLConnection connection = location.openConnection();
                if (connection instanceof JarURLConnection) {
                    JarFile jarFile = ((JarURLConnection) connection).getJarFile();
                    String name = jarFile.getName();
                    //name类似于:/xiaoyun-core/target/xiaoyun-core-0.0.1.jar!/BOOT-INF/classes
                    int separator = name.indexOf("!/");
                    if (separator > 0) {
                        name = name.substring(0, separator);
                    }
                    source = new File(name);
                } else {
                    source = new File(location.toURI().getPath());
                }
            } catch (Exception e) {
                logger.error("获得系统根目录时失败," + e.getMessage(), e);
            }
        }
        String path = null;
        if (source != null && source.exists()) {
            path = source.getParentFile().getAbsoluteFile().getPath();
        }
        if (path == null) {
            throw new XyException("获得系统根目录失败");
        }
        basePath = Paths.get(path);
    }

}
