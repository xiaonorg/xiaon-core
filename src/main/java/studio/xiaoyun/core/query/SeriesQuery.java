package studio.xiaoyun.core.query;

public class SeriesQuery extends AbstractQuery {
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
