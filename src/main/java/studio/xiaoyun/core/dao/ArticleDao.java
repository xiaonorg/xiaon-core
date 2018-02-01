package studio.xiaoyun.core.dao;

import studio.xiaoyun.core.pojo.ArticleDO;
import studio.xiaoyun.core.query.ArticleQuery;

import java.util.List;

/**
 * 文章相关
 */
public interface ArticleDao extends AbstractDao<ArticleDO> {

    /**
     * 获得文章信息
     * @param parameter 搜索参数
     * @return 文章信息
     */
    List<ArticleDO> listArticleByParameter(ArticleQuery parameter);

    /**
     * 文章的数量
     * @param parameter　搜索参数
     * @return 文章的数量
     */
    long countArticleByParameter(ArticleQuery parameter);
}
