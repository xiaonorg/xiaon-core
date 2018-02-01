package studio.xiaoyun.app.convert.tool;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import studio.xiaoyun.app.convert.converter.IHtmlConverter;
import studio.xiaoyun.core.exception.NotImplementedException;
import studio.xiaoyun.core.exception.XyException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * 使用POI作为转换器实现转换功能
 */
@Service
public class PoiTool implements IHtmlConverter {
    private static Logger log = LoggerFactory.getLogger(PoiTool.class);
    private List<String> supportedExtensionsForHtmlConverter;
    /**
     * 存放图片的路径
     */
    private final static String IMAGE_PATH = "image";

    @Override
    public void docToHtml(String sourceFileName, String targetFileName) {
        try {
            initFolder(targetFileName);
            String imagePathStr = initImageFolder(targetFileName);
            HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(sourceFileName));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
            HtmlPicturesManager picturesManager = new HtmlPicturesManager(imagePathStr,IMAGE_PATH);
            wordToHtmlConverter.setPicturesManager(picturesManager);
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(new File(targetFileName));

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
        } catch (Exception e) {
            log.error("将doc文件转换为html时出错", e);
            throw new XyException("将doc文件转换为html时出错!",e);
        }
    }

    @Override
    public void docxToHtml(String sourceFileName, String targetFileName) {
        OutputStreamWriter outputStreamWriter = null;
        try {
            initFolder(targetFileName);
            String imagePathStr = initImageFolder(targetFileName);
            XWPFDocument document = new XWPFDocument(new FileInputStream(sourceFileName));
            XHTMLOptions options = XHTMLOptions.create();
            //存放图片的文件夹
            options.setExtractor( new FileImageExtractor( new File(imagePathStr ) ) );
            //html中图片的路径
            options.URIResolver( new BasicURIResolver(IMAGE_PATH));
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(targetFileName),"utf-8");
            XHTMLConverter xhtmlConverter = (XHTMLConverter)XHTMLConverter.getInstance();
            xhtmlConverter.convert(document, outputStreamWriter, options);
        } catch (Exception e) {
            log.error("将docx文件转换为html时出错", e);
            throw new XyException("将docx文件转换为html时出错!",e);
        }finally{
            try{
                if(outputStreamWriter!=null){
                    outputStreamWriter.close();
                }
            }catch(Exception e){
                log.error("关闭文件流时出错", e);
            }
        }
    }

    @Override
    public void xlsToHtml(String sourceFileName, String targetFileName) {
        try {
            initFolder(targetFileName);
            Document doc = ExcelToHtmlConverter.process( new File( sourceFileName) );
            DOMSource domSource = new DOMSource( doc );
            StreamResult streamResult = new StreamResult( new File(targetFileName) );
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty( OutputKeys.ENCODING, "UTF-8" );
            serializer.setOutputProperty( OutputKeys.INDENT, "yes" );
            serializer.setOutputProperty( OutputKeys.METHOD, "html" );
            serializer.transform( domSource, streamResult );
        }catch (Exception e) {
            log.error("将xls文件转换为html时出错", e);
            throw new XyException("将xls文件转换为html时出错!",e);
        }
    }

    @Override
    public void xlsxToHtml(String sourceFileName, String targetFileName) {
        throw new NotImplementedException();
    }

    @Override
    public void pptToHtml(String sourceFileName, String targetFileName) {
        throw new NotImplementedException();
    }

    @Override
    public void pptxToHtml(String sourceFileName, String targetFileName) {
        throw new NotImplementedException();
    }

    /**
     * 初始化存放html文件的文件夹
     * @param targetFileName html文件的文件名
     */
    void initFolder (String targetFileName)throws IOException{
        Path path = Paths.get(targetFileName);
        //如果文件存在，则删除文件
        Files.deleteIfExists(path);
        Path parentPath = path.getParent();
        //如果父文件夹不存在，则创建文件夹
        if(Files.notExists(parentPath)){
            Files.createDirectories(parentPath);
        }
    }

    /**
     * 初始化存放图片的文件夹
     * @param htmlFileName html文件的文件名
     * @return 存放图片的文件夹路径
     */
    String initImageFolder(String htmlFileName)throws IOException{
        Path filePath = Paths.get(htmlFileName);
        Path imagePath = filePath.getParent().resolve(IMAGE_PATH);
        Files.deleteIfExists(imagePath);
        Files.createDirectories(imagePath);
        return imagePath.toString();
    }
    
    @Override
	public List<String> getSupportedExtensionsForHtml() {
        if(supportedExtensionsForHtmlConverter==null){
            supportedExtensionsForHtmlConverter = Arrays.asList("doc","docx","xls");
        }
		return supportedExtensionsForHtmlConverter;
	}

	@Override
	public void setSupportedExtensionsForHtml(List<String> supportedExtensionsForHtmlConverter) {
		this.supportedExtensionsForHtmlConverter = supportedExtensionsForHtmlConverter;
	}

    /**
     * 读取doc文件的文本，不带格式
     * @param fileName 文件名
     * @return 文件的文本内容
     */
    public static String readTextForDoc(String fileName){
        String text;
        try(
                FileInputStream in = new FileInputStream(fileName);
                WordExtractor wordExtractor = new WordExtractor(in);
        ){
            text = wordExtractor.getText();
        }catch(Exception e){
            log.error("读取文件出错:"+fileName, e);
            throw new XyException("读取文件出错",e);
        }
        return text;
    }

    /**
     * 读取docx文件的内容，只读取纯文本内容，不带格式
     * @param fileName 文件名
     * @return 文件内容
     */
    public static String readTextForDocx(String fileName) {
        String text = null;
        POIXMLTextExtractor ex = null;
        try{
            OPCPackage oPCPackage = POIXMLDocument.openPackage(fileName);
            XWPFDocument xwpf = new XWPFDocument(oPCPackage);
            ex = new XWPFWordExtractor(xwpf);
            text = ex.getText();
        }catch(Exception e){
            log.error("读取文件出错,文件名:"+fileName, e);
            throw new XyException("读取文件出错",e);
        }finally{
            try{
                if(ex!=null){
                    ex.close();
                }
            }catch(Exception e){
                log.error("关闭流出错:"+fileName, e);
            }
        }
        return text;
    }

}
