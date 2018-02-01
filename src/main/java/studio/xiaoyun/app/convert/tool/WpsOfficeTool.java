package studio.xiaoyun.app.convert.tool;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.xiaoyun.core.exception.NotImplementedException;
import studio.xiaoyun.core.exception.XyException;
import studio.xiaoyun.app.convert.converter.IHtmlConverter;
import studio.xiaoyun.app.convert.converter.IPdfConverter;

import java.util.Collections;
import java.util.List;

/**
 * 使用wps office作为转换器将文件转换为其它格式
 */
public class WpsOfficeTool implements IPdfConverter, IHtmlConverter {
    private Logger logger = LoggerFactory.getLogger(WpsOfficeTool.class);
    private List<String> supportedExtensionsForPdf;
    private List<String> supportedExtensionsForHtml;

    @Override
    public void docToPdf(String sourceFilePath, String targetFilePath) {
        wordSaveAs(sourceFilePath, targetFilePath, 17);
    }

    @Override
    public void docxToPdf(String sourceFilePath, String targetFilePath) {
        wordSaveAs(sourceFilePath, targetFilePath, 17);
    }

    @Override
    public void xlsToPdf(String sourceFilePath, String targetFilePath) {
        ActiveXComponent et = null;
        Dispatch workbooks;
        Dispatch workbook = null;
        ComThread.InitSTA();//初始化COM线程
        try {
            et = new ActiveXComponent("KET.Application");//初始化et.exe程序
            et.setProperty("Visible", new Variant(false));
            workbooks = et.getProperty("Workbooks").toDispatch();
            Object[] params = new Object[]{sourceFilePath, 0, true};
            workbook = Dispatch.invoke(workbooks, "Open", Dispatch.Method, params, new int[1]).toDispatch();
            Dispatch.call(workbook, "ExportAsFixedFormat", 0, targetFilePath);
        } catch (Exception e) {
            logger.error("转换文件失败：" + sourceFilePath, e);
            throw new XyException("文件转换失败", e);
        } finally {
            if (workbook != null) {
                Dispatch.call(workbook, "Close");
                workbook.safeRelease();
            }
            if (et != null) {
                et.invoke("Quit");
                et.safeRelease();
            }
            ComThread.Release();
        }
    }

    @Override
    public void xlsxToPdf(String sourceFilePath, String targetFilePath) {
        xlsToPdf(sourceFilePath, targetFilePath);
    }

    @Override
    public void pptToPdf(String sourceFileName, String targetFileName) {
        ActiveXComponent activeXComponent = null;
        ActiveXComponent workbook = null;
        try {
            ComThread.InitSTA();//初始化COM线程
            activeXComponent = new ActiveXComponent("KWPP.Application");//初始化exe程序
            workbook = activeXComponent.invokeGetComponent("Presentations").invokeGetComponent
                    ("Open", new Variant(sourceFileName), new Variant(true));
            workbook.invoke("SaveAs", new Variant(targetFileName), new Variant(32));
        } catch (Exception e) {
            logger.error("转换文件失败：" + sourceFileName, e);
            throw new XyException("文件转换失败", e);
        } finally {
            if (workbook != null) {
                workbook.invoke("Close");
                workbook.safeRelease();
            }
            if (activeXComponent != null) {
                activeXComponent.invoke("Quit");
                activeXComponent.safeRelease();
            }
            ComThread.Release();
        }
    }

    @Override
    public void pptxToPdf(String sourceFilePath, String targetFilePath) {
        pptToPdf(sourceFilePath, targetFilePath);
    }

    @Override
    public List<String> getSupportedExtensionsForPdf() {
        return supportedExtensionsForPdf == null ? Collections.emptyList() : supportedExtensionsForPdf;
    }

    @Override
    public void setSupportedExtensionsForPdf(List<String> supportedExtensionsForPdf) {
        this.supportedExtensionsForPdf = supportedExtensionsForPdf;
    }

    @Override
    public void docToHtml(String sourceFileName, String targetFileName) {
        wordSaveAs(sourceFileName, targetFileName, 8);
    }

    @Override
    public void docxToHtml(String sourceFileName, String targetFileName) {
        wordSaveAs(sourceFileName, targetFileName, 8);
    }

    @Override
    public List<String> getSupportedExtensionsForHtml() {
        return supportedExtensionsForHtml == null ? Collections.emptyList() : supportedExtensionsForHtml;
    }

    @Override
    public void setSupportedExtensionsForHtml(List<String> extensions) {
        supportedExtensionsForHtml = extensions;
    }

    @Override
    public void xlsToHtml(String sourceFileName, String targetFileName) {
        throw new NotImplementedException();
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
     * word另存为其它格式的文件
     * @param sourceFileName 源文件名,doc或者docx文件
     * @param targetFileName 转换后的文件名
     * @param type           类型,7 转换为txt格式，8 转换为html格式，17 转换为pdf格式
     */
    public void wordSaveAs(String sourceFileName, String targetFileName, int type) {
        String extension = FileTool.getFileExtension(targetFileName);
        ActiveXComponent activeXComponent = null;
        ActiveXComponent workbook = null;
        try {
            ComThread.InitSTA();//初始化COM线程
            activeXComponent = new ActiveXComponent("KWPS.Application");//初始化exe程序
            Variant[] openParams = new Variant[]{
                    new Variant(sourceFileName),
                    new Variant(true),
                    new Variant(true)//readOnley
            };
            workbook = activeXComponent.invokeGetComponent("Documents").invokeGetComponent
                    ("Open", openParams);
            workbook.invoke("SaveAs", new Variant(targetFileName), new Variant(type));
        } catch (Exception e) {
            logger.error("转换文件失败：" + sourceFileName, e);
            throw new XyException("文件转换失败", e);
        } finally {
            if (workbook != null) {
                workbook.invoke("Close");
                workbook.safeRelease();
            }
            if (activeXComponent != null) {
                activeXComponent.invoke("Quit");
                activeXComponent.safeRelease();
            }
            ComThread.Release();
        }
    }
}
