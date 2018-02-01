package studio.xiaoyun.app.convert.tool;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.xiaoyun.core.exception.XyException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileTool {
    private static Logger log = LoggerFactory.getLogger(FileTool.class);
    private FileTool(){}

    /**
     * 从文件名中截取文件扩展名
     * @param fileName 文件名
     * @return 文件扩展名的小写形式，如果文件没有扩展名，则返回长度为0的空字符串
     */
    public static String getFileExtension(String fileName){
        int index = fileName.lastIndexOf('.');
        if(index>0){
            return fileName.substring(index+1).toLowerCase();
        }else{
            return "";
        }
    }
    
    /**
     * 根据utf-8编码读取纯文本文件的内容
     * @param fileName 文件名
     * @return 文件内容
     */
    public static String readFile(Path fileName){
        File file = new File(fileName.toString());
        StringBuilder sb = new StringBuilder((int)file.length());
        try(
        		FileInputStream inputStream = new FileInputStream(file);
        		InputStreamReader in = new InputStreamReader(inputStream,Charset.forName("utf-8"))
           ){
            char[] b = new char[1024];
            int n;
            while((n=in.read(b))!=-1){
                sb.append(b,0,n);
            }
        }catch(Exception e){
            log.error("读入文件时出现错误",e);
            throw new XyException("读入文件时出现错误,"+e.getMessage(),e);
        }
        return sb.toString();
    }

    /**
     * 根据utf-8编码写文件。
     * <p>如果文件已经存在，则会覆盖原文件</p>
     * @param fileName 文件名
     * @param fileContent 文件内容
     */
    public static void writeFile(Path fileName,String fileContent){
        try {
            Files.deleteIfExists(fileName); //如果文件已存在，则删除文件
            if(!Files.exists(fileName.getParent())){  //如果文件夹不存在，则创建文件夹
                Files.createDirectories(fileName.getParent());
            }
        } catch (IOException e) {
            log.error("删除文件时出错", e);
            throw new XyException("删除文件时出错!"+e.getMessage(),e);
        }
        try(
        		FileOutputStream outputStream = new FileOutputStream(fileName.toString());
        		OutputStreamWriter out = new OutputStreamWriter(outputStream,Charset.forName("utf-8"))
            ) {
        	out.write(fileContent);
        }catch (Exception e) {
            log.error("写文件时出错", e);
            throw new XyException("写文件时出错!"+e.getMessage(),e);
        }
    }
    
}
