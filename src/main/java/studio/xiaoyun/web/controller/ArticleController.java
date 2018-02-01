package studio.xiaoyun.web.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import studio.xiaoyun.core.constant.ArticleStatus;
import studio.xiaoyun.core.dao.ArticleDao;
import studio.xiaoyun.core.dto.ArticleDTO;
import studio.xiaoyun.core.dto.DTOConverter;
import studio.xiaoyun.core.pojo.ArticleDO;
import studio.xiaoyun.core.query.ArticleQuery;
import studio.xiaoyun.core.service.ArticleService;
import studio.xiaoyun.security.annotation.RequireUser;
import studio.xiaoyun.web.ParameterUtil;
import studio.xiaoyun.web.WebResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/v1/article")
public class ArticleController {
    @Resource
    private ArticleDao articleDao;
    @Resource
    private DTOConverter converter;
    @Resource
    private ArticleService articleService;

    /**
     * 创建文章
     * @param param json格式的数据
     * @return 文章信息
     */
    @RequireUser
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ArticleDTO saveArticle(String param) {
        ArticleDTO param2 = JSON.parseObject(param, ArticleDTO.class);
        String articleId = articleService.saveArticle(param2);
        ArticleDO articleDO = articleDao.getById(articleId);
        ArticleDTO resource = converter.toDto(articleDO, null, ArticleDTO.class);
        return resource;
    }

    /**
     * 根据id获得文章的信息
     * @param request   请求
     * @param articleId 文章id
     * @return 文章的信息
     */
    @RequestMapping(value = "/{Id:\\S{32}}", method = RequestMethod.GET)
    public ArticleDTO getArticleById(HttpServletRequest request, @PathVariable("Id") String articleId) {
        ArticleQuery param = ParameterUtil.getParameter(request, ArticleQuery.class);
        ArticleDTO resource = articleService.getArticleById(articleId);
        converter.setNull(Collections.singletonList(resource), param);
        return resource;
    }

    /**
     * 搜索文章
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public WebResult getArticleByParameter(HttpServletRequest request) {
        ArticleQuery param = ParameterUtil.getParameter(request, ArticleQuery.class);
        long count = articleService.countArticleByParameter(param);
        List<ArticleDTO> articleList = articleService.listArticleByParameter(param);
        return new WebResult(count, articleList);
    }


    /**
     * 修改文章状态
     * @param articleId 文章id
     * @param status    状态
     */
    @RequireUser
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{Id:\\S{32}}/status", method = RequestMethod.POST)
    public void updateArticleStatus(@PathVariable("Id") String articleId, String status) {
        articleService.updateArticleStatus(articleId, ArticleStatus.valueOf(status));
    }

    /**
     * 修改文章信息
     * @param articleId 文章id
     * @param param     文章信息
     */
    @RequireUser
    @RequestMapping(value = "/{Id:\\S{32}}", method = RequestMethod.POST)
    public ArticleDTO updateArticle(@PathVariable("Id") String articleId, String param) {
        ArticleDTO param2 = JSON.parseObject(param, ArticleDTO.class);
        ArticleDO articleDO = converter.toDo(param2, ArticleDO.class);
        articleDO.setArticleId(articleId);
        articleService.updateArticle(articleDO);
        ArticleDTO resource = articleService.getArticleById(articleId);
        return resource;
    }

    /**
     * 删除文章
     * @param articleId 文章id
     */
    @RequireUser
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{Id:\\S{32}}", method = RequestMethod.DELETE)
    public void deleteArticle(@PathVariable("Id") String articleId) {
        articleService.deleteArticle(articleId);
    }

}
