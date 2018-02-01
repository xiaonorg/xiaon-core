package studio.xiaoyun.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import studio.xiaoyun.app.convert.tool.FileTool;
import studio.xiaoyun.config.SystemConfig;
import studio.xiaoyun.core.dto.UploadFileDTO;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.exception.XyException;
import studio.xiaoyun.web.WebException;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

/**
 * 文件相关
 */
@Controller
@RequestMapping("/v1/file")
public class FileController {
    private Logger logger = LoggerFactory.getLogger(FileController.class);
    @Resource
    private SystemConfig config;

    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @return 文件相关信息
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public UploadFileDTO uploadFile(MultipartFile file) {
        if (logger.isDebugEnabled()) {
            logger.debug("上传文件:" + file.getOriginalFilename());
        }
        String extension = FileTool.getFileExtension(file.getOriginalFilename());
        if (!config.getAllowedExtension().contains(extension)) {
            throw new InvalidParameterException("不支持的文件类型:" + extension);
        }
        if (file.getSize() > 2 * 1024 * 1024) { //文件大小不能超过2MB
            throw new InvalidParameterException("文件太大");
        }
        String filePath = config.getUploadPath() + UUID.randomUUID().toString();
        if (StringUtils.isNotEmpty(extension)) {
            filePath += "." + extension;
        }
        File newFile = config.getFilePath().resolve(filePath).toFile();
        try {
            file.transferTo(newFile);
        } catch (Exception e) {
            logger.error("上传文件失败!", e);
            throw new WebException("上传失败");
        }
        UploadFileDTO resource = new UploadFileDTO();
        resource.setOriginalFileName(file.getOriginalFilename());
        resource.setNewFileName(Paths.get(filePath).getFileName().toString());
        String url = File.separator + filePath;
        if (File.separator.equals("\\")) {
            url = url.replaceAll("\\\\", "/");
        }
        resource.setUrl(url);
        return resource;
    }

    /**
     * 上传图片
     *
     * @param file 图片相关信息
     * @return 图片地址、名称等信息
     */
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public UploadFileDTO uploadImage(MultipartFile file) {
        String extension = FileTool.getFileExtension(file.getOriginalFilename());
        if (!extension.matches("png|jpg|jpeg")) {
            throw new InvalidParameterException("文件格式不支持");
        }
        UploadFileDTO uploadFile = uploadFile(file);
        Path sourceFile = config.getFilePath().resolve(uploadFile.getUrl().substring(1));
        LocalDate date = LocalDate.now();
        Path datePath = Paths.get(String.valueOf(date.getYear()), String.valueOf(date.getMonthValue()), String.valueOf(date.getDayOfMonth()));
        // 图片路径=文件根目录+图片目录+日期{yyyy/m/dd}+图片名
        Path newFile = Paths.get(config.getImagePath()).resolve(datePath).resolve(uploadFile.getNewFileName());
        try {
            if (Files.notExists(config.getFilePath().resolve(newFile).getParent())) {
                Files.createDirectories(config.getFilePath().resolve(newFile).getParent());
            }
            Files.copy(sourceFile, config.getFilePath().resolve(newFile));
            Files.delete(sourceFile);
        } catch (IOException e) {
            throw new XyException("复制图片失败，文件:" + sourceFile, e);
        }
        String url = newFile.toString();
        if (File.separator.equals("\\")) {
            url = url.replaceAll("\\\\", "/");
        }
        uploadFile.setUrl("/" + url);
        return uploadFile;
    }
}
