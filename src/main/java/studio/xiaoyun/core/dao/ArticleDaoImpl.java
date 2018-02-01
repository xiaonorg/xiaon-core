package studio.xiaoyun.core.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.pojo.ArticleDO;
import studio.xiaoyun.core.query.ArticleQuery;

import java.util.List;

@Repository("articleDao")
public class ArticleDaoImpl extends AbstractDaoImpl<ArticleDO> implements ArticleDao {

    @Override
    public List<ArticleDO> listArticleByParameter(ArticleQuery parameter) {
        return listByParameter(null,null,parameter,ArticleDO.class);
    }

    @Override
    public long countArticleByParameter(ArticleQuery parameter) {
        return countByParameter(null,null,parameter);
    }

    @Override
    public ArticleDO getById(String id) throws InvalidParameterException {
        ArticleDO article = getSession().get(ArticleDO.class, id);
        if(article==null){
            throw new InvalidParameterException(id+"不存在");
        }
        return article;
    }

    @Override
    public ArticleDO loadById(String id) {
        return getSession().load(ArticleDO.class, id);
    }

    @Override
    String getQuerySql() {
        return "select article_0.* from xy_article as article_0";
    }
}
