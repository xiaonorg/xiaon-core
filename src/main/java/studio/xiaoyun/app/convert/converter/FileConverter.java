package studio.xiaoyun.app.convert.converter;

import org.springframework.stereotype.Service;
import studio.xiaoyun.core.BeanFactory;
import studio.xiaoyun.config.SystemConfig;
import studio.xiaoyun.core.exception.XyException;
import studio.xiaoyun.app.convert.tool.FileTool;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * 将一种文件格式转换为其它格式
 */
@Service
public class FileConverter {
    @Resource
    private BeanFactory beanFactory;
    @Resource
    private SystemConfig config;
	private List<IHtmlConverter> htmlConverterList;
	private List<IPdfConverter> pdfConverterList;

    /**
     * 将文件转换为html文件
     * @param fileName 文件名
     * @param title html文件的标题
     * @return html文件的文件名
     * @throws XyException 如果转换失败，则抛出异常
     */
    public String toHtml(String fileName,String title) throws XyException {
    	if(htmlConverterList==null){
            htmlConverterList = beanFactory.getBeansOfType(IHtmlConverter.class);
    	}
        String extension = FileTool.getFileExtension(fileName);
        String newFileName = config.getFilePath().resolve(config.getConvertPath())
                .resolve(UUID.randomUUID().toString()).resolve("index.html").toString();
        //遍历实现IHtmlConverter接口的bean，查找支持指定文件扩展名的一个bean
        Optional<IHtmlConverter> converter = htmlConverterList.stream()
        		.filter(item->item.isSupportedExtensionsForHtml(extension)).findAny();
        if(!converter.isPresent()){
            throw new XyException("不支持的文件类型:"+extension);
        }else{
        	converter.get().toHtml(fileName,newFileName,title);
        }
        return newFileName;
    }

    /**
     * 将文件转换为pdf文件
     * @param fileName 文件名
     * @return pdf文件的文件名
     * @throws XyException 如果转换失败，则抛出异常
     */
    public String toPdf(String fileName)throws XyException {
    	if(pdfConverterList==null){
            pdfConverterList = beanFactory.getBeansOfType(IPdfConverter.class);
    	}
        File file = new File(fileName);
        String extension = FileTool.getFileExtension(fileName);
        String newFileName = config.getFilePath().resolve(config.getConvertPath())
                .resolve("pdf").resolve(UUID.randomUUID().toString()+".pdf").toString();
        //遍历实现IPdfConverter接口的bean，查找支持指定文件扩展名的一个bean
        Optional<IPdfConverter> converter = pdfConverterList.stream()
        		.filter(item->item.isSupportedExtensionsForPdf(extension)).findAny();
        if(!converter.isPresent()){
            throw new XyException("不支持的文件类型:"+extension);
        }else{
        	converter.get().toPdf(fileName,newFileName);
        }
        return newFileName;
    }

}
