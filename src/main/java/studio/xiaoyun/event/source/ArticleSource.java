package studio.xiaoyun.event.source;

/**
 * 文章相关事件使用的事件源
 */
public class ArticleSource {
    private String aritcleId;
    private String title;

    public String getAritcleId() {
        return aritcleId;
    }

    public void setAritcleId(String aritcleId) {
        this.aritcleId = aritcleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
