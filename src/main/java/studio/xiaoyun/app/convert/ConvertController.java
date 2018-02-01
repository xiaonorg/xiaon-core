package studio.xiaoyun.app.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import studio.xiaoyun.app.convert.converter.FileConverter;
import studio.xiaoyun.config.SystemConfig;
import studio.xiaoyun.core.dto.UploadFileDTO;
import studio.xiaoyun.web.controller.FileController;

import javax.annotation.Resource;
import java.io.File;

@RestController
@RequestMapping("/v1")
public class ConvertController {
    private Logger logger = LoggerFactory.getLogger(ConvertController.class);
    @Resource
    private FileConverter fileConverter;
    @Resource
    private SystemConfig config;
    @Resource
    private FileController fileController;

    /**
     * 上传文件并将文件转换为html格式
     * @param file 文件
     * @return 文件相关信息
     */
    @RequestMapping(value="/file/toHtml",method = RequestMethod.POST)
    public UploadFileDTO toHtml(MultipartFile file){
        //上传文件
        UploadFileDTO resource = fileController.uploadFile(file);
        String filePath = resource.getUrl();
        if(logger.isDebugEnabled()){
            logger.debug("预览文件,文件路径:"+filePath);
        }
        String fileName = config.getFilePath().resolve(filePath.substring(1)).toString();
        String htmlFileName = fileConverter.toHtml(fileName,resource.getOriginalFileName());  //将文件转换为html
        String url = htmlFileName.substring(htmlFileName.indexOf(config.getConvertPath())-1);
        if(File.separator.equals("\\")){
            url = url.replaceAll("\\\\","/");
        }
        resource.setUrl(url);
        return resource;
    }

}
