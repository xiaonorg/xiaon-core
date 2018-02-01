package studio.xiaoyun.core.dto;

import java.util.List;

/**
 * 树结构的系列
 */
public class SeriesTreeDTO implements AbstractDTO {
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
     * 子节点
     */
    private List<SeriesTreeDTO> childSeries;

    /**
     * 关联的文章
     */
    private ArticleDTO article;

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

    public List<SeriesTreeDTO> getChildSeries() {
        return childSeries;
    }

    public void setChildSeries(List<SeriesTreeDTO> childSeries) {
        this.childSeries = childSeries;
    }

    public ArticleDTO getArticle() {
        return article;
    }

    public void setArticle(ArticleDTO article) {
        this.article = article;
    }
}
