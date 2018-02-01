package studio.xiaoyun.core.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 *  文章系列
 */
@Entity
@Table(name = "xy_series")
public class SeriesDO {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "studio.xiaoyun.core.pojo.UuidIdentifierGenerator")
    @Column(length = 32, name = "seriesid")
    private String seriesId;

    /**
     * 名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 序号,从1开始
     */
    @Column(nullable = false,name="ordernum")
    private Integer orderNum;

    /**
     * 父节点的id，为null时表示是根节点
     */
    @Column(name = "parentid")
    private String parentId;

    /**
     * 根节点的id，为null时表示是根节点
     */
    @Column(name = "rootid")
    private String rootId;

    /**
     * 关联文章的id
     */
    @Column(name = "articleid")
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

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
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
}
