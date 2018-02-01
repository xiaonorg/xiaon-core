package studio.xiaoyun.core.pojo;

import org.hibernate.annotations.GenericGenerator;
import studio.xiaoyun.core.constant.ArticleStatus;
import studio.xiaoyun.core.constant.ArticleType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 文章
 */
@Entity
@Table(name = "xy_article")
public class ArticleDO {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "studio.xiaoyun.core.pojo.UuidIdentifierGenerator")
    @Column(length = 32, name = "articleid")
    private String articleId;

    /**
     * 文章的标题
     */
    @Column(nullable = false,length = 100)
    private String title;

    /**
     * 文章的简介
     */
    @Column(nullable = false,length = 500)
    private String intro;

    /**
     * 用户输入的原始文本
     */
    @Transient
    private String originalText;

    /**
     * 格式化后的html文本
     */
    @Transient
    private String formatText;

    /**
     * 创建时间
     */
    @Column(nullable = false, name = "createdate")
    private LocalDateTime createDate;

    /**
     * 最后修改时间
     */
    @Column(nullable = false, name = "updatedate")
    private LocalDateTime updateDate;

    /**
     * 创建人
     */
    @Column(nullable = false,length = 32)
    private String creator;

    /**
     * 文章的状态
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleStatus status;

    /**
     * 文章类型
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleType type;

    /**
     * 文章标签
     */
    @ManyToMany(targetEntity=LabelDO.class)
    @JoinTable(name = "xy_article_label",
            joinColumns = @JoinColumn(name = "articleid"),
            inverseJoinColumns = @JoinColumn(name = "labelid"))
    private Set<LabelDO> labels;

    /**
     * 访问文章的url
     */
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<LabelDO> getLabels() {
        return labels;
    }

    public void setLabels(Set<LabelDO> labels) {
        this.labels = labels;
    }

    public ArticleType getType() {
        return type;
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

    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
