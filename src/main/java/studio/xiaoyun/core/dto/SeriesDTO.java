package studio.xiaoyun.core.dto;

/**
 * 文章系列
 */
public class SeriesDTO implements AbstractDTO {
    private String seriesId;

    /**
     * 名称
     */
    private String name;

    /**
     * 序号
     */
    private Integer orderNum;
    /**
     * 父节点的id，为null时表示是根节点
     */
    private String parentId;

    private String rootId;

    /**
     * 关联文章的id
     */
    private String articleId;

    /**
     * 关联文章的名称
     */
    private String articleName;

    /**
     * 树节点的层树，从1开始
     */
    private Integer level;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}
