package studio.xiaoyun.event.event;

import reactor.bus.Event;
import studio.xiaoyun.event.source.ArticleSource;

/**
 * 文章发布事件
 */
public class ArticleReleaseEvent extends Event<ArticleSource> {
    public static final String EVENT_NAME = "article_release";
    public ArticleReleaseEvent(ArticleSource data) {
        super(data);
    }
}
