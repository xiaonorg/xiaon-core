package studio.xiaoyun.event.event;

import reactor.bus.Event;
import studio.xiaoyun.event.source.ArticleSource;

/**
 * 文章删除事件
 */
public class ArticleDeleteEvent extends Event<ArticleSource> {
    public static final String EVENT_NAME = "article_delete";
    public ArticleDeleteEvent(ArticleSource data) {
        super(data);
    }
}
