package studio.xiaoyun.core.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.bus.EventBus;
import studio.xiaoyun.config.SystemConfig;
import studio.xiaoyun.core.constant.ArticleStatus;
import studio.xiaoyun.core.dao.ArticleDao;
import studio.xiaoyun.core.dao.SeriesDao;
import studio.xiaoyun.core.dao.UserDao;
import studio.xiaoyun.core.dto.ArticleDTO;
import studio.xiaoyun.core.dto.DTOConverter;
import studio.xiaoyun.core.pojo.ArticleDO;
import studio.xiaoyun.core.pojo.SeriesDO;
import studio.xiaoyun.core.pojo.UserDO;
import studio.xiaoyun.core.query.ArticleQuery;
import studio.xiaoyun.app.convert.tool.FileTool;
import studio.xiaoyun.core.query.SeriesQuery;
import studio.xiaoyun.core.query.criterion.Query;
import studio.xiaoyun.event.event.ArticleDeleteEvent;
import studio.xiaoyun.event.event.ArticleReleaseEvent;
import studio.xiaoyun.event.source.ArticleSource;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService{
    private Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    @Resource
    private ArticleDao articleDao;
    @Resource
    private UserDao userDao;
    @Resource
    private SystemConfig config;
    @Resource
    private DTOConverter converter;
    @Resource
    private EventBus eventBus;
    @Resource
    private SeriesDao seriesDao;

    @Override
    public String saveArticle(ArticleDTO param) {
        param.setFormatText(this.formatHtml(param.getFormatText()));
        String text = parserText(param.getFormatText());
        Subject subject = SecurityUtils.getSubject();
        ArticleDO article = new ArticleDO();
        article.setCreator(subject.getPrincipal().toString());
        article.setTitle(param.getTitle());
        article.setIntro(text);
        article.setType(param.getType());
        article.setCreateDate(LocalDateTime.now());
        article.setUpdateDate(article.getCreateDate());
        article.setStatus(param.getStatus());
        if(StringUtils.isNotBlank(param.getUrl())){
            article.setUrl(param.getUrl());
        }
        String articleId = articleDao.save(article);
        FileTool.writeFile(getOriginalPath(articleId,article.getCreateDate()),param.getOriginalText());
        FileTool.writeFile(getFormatPath(articleId,article.getCreateDate()),param.getFormatText());
        return articleId;
    }

    @Override
    public ArticleDTO getArticleById(String articleId) {
        ArticleDO article = articleDao.getById(articleId);
        Path originalPath = getOriginalPath(articleId,article.getCreateDate());
        Path formatPath = getFormatPath(articleId,article.getCreateDate());
        if(Files.exists(originalPath)){
            article.setOriginalText(FileTool.readFile(originalPath));
        }else{
            article.setOriginalText(" ");
        }
        if(Files.exists(formatPath)){
            article.setFormatText(FileTool.readFile(formatPath));
        }else{
            article.setFormatText(" ");
        }
       ArticleDTO dto = converter.toDto(article,null,ArticleDTO.class);
        UserDO user =  userDao.getById(dto.getCreator());
        dto.setCreatorName(user.getName());
        //获得文章所属的系列
        SeriesQuery seriesQuery = new SeriesQuery();
        seriesQuery.addQuery(Query.equals("articleId",articleId));
        List<SeriesDO> seriesDOList = seriesDao.listSeriesByParameter(seriesQuery);
        if(seriesDOList.size()>0){
            dto.setSeriesId(seriesDOList.get(0).getSeriesId());
        }
        return dto;
    }

    @Override
    public void updateArticle(ArticleDO param) {
        param.setFormatText(this.formatHtml(param.getFormatText()));
        ArticleDO article = articleDao.getById(param.getArticleId());
        String text = parserText(param.getFormatText());
        article.setTitle(param.getTitle());
        article.setIntro(text);
        article.setType(param.getType());
        article.setUpdateDate(LocalDateTime.now());
        article.setStatus(param.getStatus());
        if(StringUtils.isNotBlank(param.getUrl())){
            article.setUrl(param.getUrl());
        }
        FileTool.writeFile(getOriginalPath(article.getArticleId(),article.getCreateDate()),param.getOriginalText());
        FileTool.writeFile(getFormatPath(article.getArticleId(),article.getCreateDate()),param.getFormatText());
        articleDao.update(article);
        // 触发事件
        if(ArticleStatus.RELEASE.equals(param.getStatus())){
            ArticleSource source = new ArticleSource();
            source.setAritcleId(article.getArticleId());
            source.setTitle(article.getTitle());
            eventBus.notify(ArticleReleaseEvent.EVENT_NAME,new ArticleReleaseEvent(source));
        }
    }

    @Override
    public void updateArticleStatus(String articleId,ArticleStatus status) {
        ArticleDO article = articleDao.getById(articleId);
        article.setStatus(status);
        articleDao.update(article);
    }

    @Override
    public void deleteArticle(String articleId) {
        ArticleDO article = articleDao.getById(articleId);
        String title = article.getTitle();
        LocalDateTime createDate = article.getCreateDate();
        articleDao.delete(articleId);
        Path originalPath = getOriginalPath(articleId,createDate);
        Path formatPath = getFormatPath(articleId,createDate);
        try{
            Files.deleteIfExists(originalPath);
            Files.deleteIfExists(formatPath);
            Files.deleteIfExists(originalPath.getParent());
        }catch(IOException e){
            logger.error("删除文章相关的文件失败,文章id:"+articleId,e);
        }
        ArticleSource source = new ArticleSource();
        source.setAritcleId(articleId);
        source.setTitle(title);
        eventBus.notify(ArticleDeleteEvent.EVENT_NAME,new ArticleDeleteEvent(source));
    }

    @Override
    public List<ArticleDTO> listArticleByParameter(ArticleQuery parameter) {
        List<ArticleDO> articleDOList = articleDao.listArticleByParameter(parameter);
        List<ArticleDTO> articleDTOList = converter.toDto(articleDOList,null,ArticleDTO.class);
        //设置文章的创建者的名称
        if(!articleDTOList.isEmpty() && parameter.getIncludeField().contains("creatorName")){
            List<String> userIds = articleDTOList.stream().map(ArticleDTO::getCreator).distinct().collect(Collectors.toList());
            List<UserDO> names = userDao.listNameByUserIds(userIds);
            articleDTOList.forEach(item->{
                Optional<UserDO> user = names.stream().filter(e->item.getCreator().equals(e.getUserId())).findFirst();
                user.ifPresent(userDO -> item.setCreatorName(userDO.getName()));
            });
        }
        //设置文章所属的系列
        if(!articleDTOList.isEmpty() && parameter.getIncludeField().contains("seriesId")){
            List<String> articleIds = articleDTOList.stream().map(ArticleDTO::getArticleId).distinct().collect(Collectors.toList());
            SeriesQuery seriesQuery = new SeriesQuery();
            seriesQuery.addQuery(Query.in("articleId",articleIds));
            List<SeriesDO> seriesDOList = seriesDao.listSeriesByParameter(seriesQuery);
            articleDTOList.forEach(item->{
                Optional<SeriesDO> series = seriesDOList.stream().filter(e->item.getArticleId().equals(e.getArticleId())).findFirst();
                series.ifPresent(s->item.setSeriesId(s.getSeriesId()));
            });
        }
        converter.setNull(articleDTOList,parameter);
        return articleDTOList;
    }

    @Override
    public long countArticleByParameter(ArticleQuery parameter) {
        return articleDao.countArticleByParameter(parameter);
    }

    /**
     * 获得存放文章原始文本的文件的路径
     * @param articleId 文章id
     * @param createDate 文章的创建时间
     * @return 文件的路径
     */
    private Path getOriginalPath(String articleId,LocalDateTime createDate){
        String[] paths = new String[6];
        paths[0] = config.getArticlePath();
        paths[1] = String.valueOf(createDate.getYear());
        paths[2] = String.valueOf(createDate.getMonthValue());
        paths[3] = String.valueOf(createDate.getDayOfMonth());
        paths[4] = articleId;
        paths[5] = "original.txt";
        return Paths.get(config.getFilePath().toString(),paths);
    }

    /**
     * 获得存放文章格式化文本的文件的路径
     * @param articleId 文章id
     * @param createDate 文章的创建时间
     * @return 文件的路径
     */
    private Path getFormatPath(String articleId,LocalDateTime createDate){
        String[] paths = new String[6];
        paths[0] = config.getArticlePath();
        paths[1] = String.valueOf(createDate.getYear());
        paths[2] = String.valueOf(createDate.getMonthValue());
        paths[3] = String.valueOf(createDate.getDayOfMonth());
        paths[4] = articleId;
        paths[5] = "format.txt";
        return Paths.get(config.getFilePath().toString(),paths);
    }

    /**
     * 从html字符串中解析文字
     * @param html html字符串
     * @return 文字
     */
    protected String parserText(String html){
        Document document = Jsoup.parse(html);
        document.select("code").remove();
        //获得纯文本，不包含html标签
        String text = document.text();
        if(text==null){
            text = " ";
        }else if(text.length()>200){
            text = text.substring(0,200);
        }
        return text;
    }

    /**
     * 格式化html
     * @param html html文本
     * @return 新html
     */
    protected String formatHtml(String html){
        String text = html.replaceAll("<br />\\s*<img"   ," <img" );
        Document doc = Jsoup.parse(text);
        doc.select("h1").wrap("<div class=\"md-h1\"><fieldset><legend></legend></fieldset></div>");
        doc.select("h1").unwrap();
        return doc.html();
    }
}
