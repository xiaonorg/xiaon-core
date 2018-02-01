package studio.xiaoyun.core.service;

import studio.xiaoyun.core.constant.ArticleStatus;
import studio.xiaoyun.core.dto.ArticleDTO;
import studio.xiaoyun.core.pojo.ArticleDO;
import studio.xiaoyun.core.query.ArticleQuery;

import java.util.List;

public interface ArticleService {

    /**
     * 保存文章
     * @param article 文章数据
     * @return 文章id
     */
    String saveArticle(ArticleDTO article);

    /**
     * 根据文章id获得文章的相关信息
     * @param articleId 文章id
     * @return 文章的相关信息
     */
    ArticleDTO getArticleById(String articleId);

    /**
     * 修改文章信息
     * @param article 文章数据
     */
    void updateArticle(ArticleDO article);

    /**
     * 更新文章的状态
     * @param articleId 文章id
     * @param status 状态
     */
    void updateArticleStatus(String articleId,ArticleStatus status);

    /**
     * 删除文章
     * @param articleId 文章id
     */
    void deleteArticle(String articleId);

    /**
     * 获得文章信息，包括文章的原始内容和格式化后的内容
     * @param parameter 搜索参数
     * @return 文章信息
     */
    List<ArticleDTO> listArticleByParameter(ArticleQuery parameter);

    /**
     * 文章的数量
     * @param parameter　搜索参数
     * @return 文章的数量
     */
    long countArticleByParameter(ArticleQuery parameter);

}
