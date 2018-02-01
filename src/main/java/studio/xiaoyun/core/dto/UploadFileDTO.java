package studio.xiaoyun.core.dto;

/**
 * 封装上传的文件相关的信息
 */
public class UploadFileDTO implements AbstractDTO {
	/**
     * 原始文件名
     */
    private String originalFileName;
    /**
     * 新的文件名
     */
    private String newFileName;
    /**
     * 指向新文件的url地址
     */
    private String url;

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UploadFileDTO{");
        sb.append("originalFileName='").append(originalFileName).append('\'');
        sb.append(", newFileName='").append(newFileName).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
