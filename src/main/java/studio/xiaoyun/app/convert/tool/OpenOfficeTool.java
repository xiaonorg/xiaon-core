package studio.xiaoyun.app.convert.tool;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

import studio.xiaoyun.app.convert.converter.IPdfConverter;
import studio.xiaoyun.core.exception.XyException;

/**
 * 使用OpenOffice作为转换器将文件转换为其它格式
 */
public class OpenOfficeTool implements IPdfConverter {
    private static Logger log = LoggerFactory.getLogger(OpenOfficeTool.class);
    private List<String> supportedExtensionsForPDFConverter;
    /**
     * OpenOffice主程序的路径
     */
    private String openOfficePath;
    
    @Override
    public void docToPdf(String sourceFilePath, String targetFilePath) {
        convert(sourceFilePath,targetFilePath);
    }

    @Override
    public void docxToPdf(String sourceFilePath, String targetFilePath) {
        convert(sourceFilePath,targetFilePath);
    }

    @Override
    public void xlsToPdf(String sourceFilePath, String targetFilePath) {
        convert(sourceFilePath,targetFilePath);
    }

    @Override
    public void xlsxToPdf(String sourceFilePath, String targetFilePath) {
        convert(sourceFilePath,targetFilePath);
    }

    @Override
    public void pptToPdf(String sourceFilePath, String targetFilePath) {
        convert(sourceFilePath,targetFilePath);
    }

    @Override
    public void pptxToPdf(String sourceFilePath, String targetFilePath) {
        convert(sourceFilePath,targetFilePath);
    }

    public void convert(String sourceFilePath, String targetFilePath) {
        File inputFile = new File(sourceFilePath);
        // 如果目标路径不存在, 则新建该路径
        File outputFile = new File(targetFilePath);
        // 启动OpenOffice的服务
        String command = getOpenOfficePath()+ " -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\" -nofirststartwizard";
        Process pro = null;
        OpenOfficeConnection connection = null;
        try{
            pro = Runtime.getRuntime().exec(command);
            connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);
            connection.connect();
            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
            converter.convert(inputFile, outputFile);
        }catch (Exception e) {
            log.error("转换文件失败：" + sourceFilePath, e);
            throw new XyException("文件转换失败");
        } finally{
            if(connection!=null){
                connection.disconnect();
            }
            if(pro!=null){
                // 关闭OpenOffice服务的进程
                pro.destroy();
            }
        }
    }
    
    @Override
	public List<String> getSupportedExtensionsForPdf() {
		return supportedExtensionsForPDFConverter==null?Collections.emptyList():supportedExtensionsForPDFConverter;
	}

	@Override
	public void setSupportedExtensionsForPdf(List<String> supportedExtensionsForPDFConverter) {
		this.supportedExtensionsForPDFConverter = supportedExtensionsForPDFConverter;
	}

	public String getOpenOfficePath() {
		if(openOfficePath==null){
			throw new XyException("请设置OpenOffice主程序的路径");
		}
		return openOfficePath;
	}

	/**
	 * 设置OpenOffice主程序的路径。
	 * <p>windows下为C:\Program Files (x86)\OpenOffice 4\program\soffice.exe
	 * @param openOfficePath OpenOffice主程序的路径
	 */
	public void setOpenOfficePath(String openOfficePath) {
		this.openOfficePath = openOfficePath;
	}

}
