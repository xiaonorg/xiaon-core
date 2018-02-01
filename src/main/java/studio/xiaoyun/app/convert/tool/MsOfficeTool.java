package studio.xiaoyun.app.convert.tool;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import studio.xiaoyun.app.convert.converter.IPdfConverter;
import studio.xiaoyun.core.exception.XyException;

/**
 * 使用微软Office作为转换器将文件转换为其它格式
 */
public class MsOfficeTool implements IPdfConverter {
    private Logger log = LoggerFactory.getLogger(MsOfficeTool.class);
    private List<String> supportedExtensionsForPDFConverter;
    
    @Override
    public void docToPdf(String sourceFilePath, String targetFilePath) {
        ActiveXComponent app = null;
        Dispatch doc = null;
        try {
            ComThread.InitSTA();
            app = new ActiveXComponent("Word.Application");
            app.setProperty("Visible", false);
            Dispatch docs = app.getProperty("Documents").toDispatch();
            doc = Dispatch.invoke(docs, "Open", Dispatch.Method,
                    new Object[]{sourceFilePath,
                            new Variant(false),
                            new Variant(true),//是否只读
                            new Variant(false),
                            new Variant("pwd")},
                    new int[1]).toDispatch();
            Dispatch.put(doc, "RemovePersonalInformation", false);
            Dispatch.call(doc, "ExportAsFixedFormat", targetFilePath, 17);
        } catch (Exception e) {
            log.error("转换文件失败：" + sourceFilePath, e);
            throw new XyException("文件转换失败",e);
        } finally {
            if (doc != null) {
                Dispatch.call(doc, "Close", false);
            }
            if (app != null) {
                app.invoke("Quit", 0);
            }
            ComThread.Release();
        }
    }

    @Override
    public void docxToPdf(String sourceFilePath, String targetFilePath) {
        docToPdf(sourceFilePath,targetFilePath);
    }

    @Override
    public void xlsToPdf(String sourceFilePath, String targetFilePath) {
        ActiveXComponent app = null;
        Dispatch excel = null;
        try {
            ComThread.InitSTA();
            app = new ActiveXComponent("Excel.Application");
            app.setProperty("Visible", false);
            Dispatch excels = app.getProperty("Workbooks").toDispatch();
            excel = Dispatch.call(excels, "Open", sourceFilePath, false, true).toDispatch();
            Dispatch.call(excel, "ExportAsFixedFormat", 0, targetFilePath);
        } catch (Exception e) {
            log.error("转换文件失败：" + sourceFilePath, e);
            throw new XyException("文件转换失败",e);
        } finally {
            if (excel != null) {
                Dispatch.call(excel, "Close");
            }
            if (app != null) {
                app.invoke("Quit");
            }
            ComThread.Release();
        }
    }

    @Override
    public void xlsxToPdf(String sourceFilePath, String targetFilePath) {
        xlsToPdf(sourceFilePath,targetFilePath);
    }

    @Override
    public void pptToPdf(String sourceFilePath, String targetFilePath) {
        ActiveXComponent app = null;
        Dispatch ppt = null;
        try {
            ComThread.InitSTA();
            app = new ActiveXComponent("PowerPoint.Application");
            Dispatch ppts = app.getProperty("Presentations").toDispatch();
            // 因POWER.EXE的发布规则为同步，所以设置为同步发布
            ppt = Dispatch.call(ppts, "Open", sourceFilePath, true,// ReadOnly
                    true,// Untitled指定文件是否有标题
                    false// WithWindow指定文件是否可见
            ).toDispatch();
            Dispatch.call(ppt, "SaveAs", targetFilePath, 32);
        } catch (Exception e) {
            log.error("转换文件失败：" + sourceFilePath, e);
            throw new XyException("文件转换失败",e);
        } finally {
            if (ppt != null) {
                Dispatch.call(ppt, "Close");
            }
            if (app != null) {
                app.invoke("Quit");
            }
            ComThread.Release();
        }
    }

    @Override
    public void pptxToPdf(String sourceFilePath, String targetFilePath) {
        pptToPdf( sourceFilePath,  targetFilePath);
    }

	@Override
	public List<String> getSupportedExtensionsForPdf() {
		return supportedExtensionsForPDFConverter==null?Collections.emptyList():supportedExtensionsForPDFConverter;
	}

	@Override
	public void setSupportedExtensionsForPdf(List<String> supportedExtensionsForPDFConverter) {
		this.supportedExtensionsForPDFConverter = supportedExtensionsForPDFConverter;
	}

}
