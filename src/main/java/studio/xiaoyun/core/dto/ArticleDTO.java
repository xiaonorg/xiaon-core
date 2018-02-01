package studio.xiaoyun.core.dto;

import studio.xiaoyun.core.constant.ArticleStatus;
import studio.xiaoyun.core.constant.ArticleType;

import java.time.LocalDateTime;

public class ArticleDTO implements AbstractDTO {
    private String articleId;

    /**
     * 文章的标题
     */
    private String title;

    /**
     * 文章的部分内容
     */
    private String intro;

    /**
     * 用户输入的原始文本
     */
    private String originalText;

    /**
     * 格式化后的html文本
     */
    private String formatText;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;

    /**
     * 最后修改时间
     */
    private LocalDateTime updateDate;

    /**
     * 创建人
     */
    private String creator;
    /**
     * 文章的状态
     */
    private ArticleStatus status;
    /**
     * 创建者的名称
     */
    private String creatorName;

    /**
     * 文章所属的系列的id
     */
    private String seriesId;

    private ArticleType type;

    private String url;

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArticleType getType() {
        return type;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setType(ArticleType type) {
        this.type = type;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getFormatText() {
        return formatText;
    }

    public void setFormatText(String formatText) {
        this.formatText = formatText;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}
