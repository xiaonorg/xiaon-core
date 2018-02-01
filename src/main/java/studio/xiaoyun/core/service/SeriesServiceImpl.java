package studio.xiaoyun.core.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import studio.xiaoyun.core.dao.ArticleDao;
import studio.xiaoyun.core.dao.SeriesDao;
import studio.xiaoyun.core.dto.DTOConverter;
import studio.xiaoyun.core.dto.SeriesDTO;
import studio.xiaoyun.core.dto.SeriesTreeDTO;
import studio.xiaoyun.core.pojo.ArticleDO;
import studio.xiaoyun.core.pojo.SeriesDO;
import studio.xiaoyun.core.query.ArticleQuery;
import studio.xiaoyun.core.query.SeriesQuery;
import studio.xiaoyun.core.query.criterion.Query;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service("seriesService")
public class SeriesServiceImpl implements SeriesService {
    @Resource
    private SeriesDao seriesDao;
    @Resource
    private DTOConverter converter;
    @Resource
    private ArticleDao articleDao;

    @Override
    public String createSeries(String name) {
        SeriesDO series = new SeriesDO();
        series.setName(name);
        series.setOrderNum(1);
        seriesDao.save(series);
        return series.getSeriesId();
    }

    @Override
    public void deleteSeries(String seriesId) {
        SeriesDO series = seriesDao.getById(seriesId);
        if (series.getRootId() == null) { //如果是根节点，则删除该系列的所有节点
            seriesDao.deleteSeriesByRootId(seriesId);
        } else { //只删除当前节点
            seriesDao.delete(series);
        }
    }

    @Override
    public void updateSeries(String rootId, List<SeriesDTO> data) {
        SeriesDO rootSeriesDO = seriesDao.getById(rootId);
        SeriesDTO rootSeriesDTO = data.stream().filter(e -> rootId.equals(e.getSeriesId())).findFirst().get();
        //更新根节点的名称
        if (!rootSeriesDO.getName().equals(rootSeriesDTO.getName())) {
            rootSeriesDO.setName(rootSeriesDTO.getName());
        }
        //更新序号
        for (int i = 1; i < 5; i++) {
            int order = 1;
            for (SeriesDTO aData : data) {
                if (aData.getLevel() == i) {
                    aData.setOrderNum(order);
                    order++;
                }
            }
        }
        SeriesQuery query = new SeriesQuery();
        query.addQuery(Query.equals("rootId", rootId));
        List<SeriesDO> oldSeriesDO = seriesDao.listSeriesByParameter(query);
        List<SeriesDO> deleteList = new ArrayList<>();
        oldSeriesDO.forEach(e -> {
            Optional<SeriesDTO> ep = data.stream().filter(d -> e.getSeriesId().equals(d.getSeriesId())).findFirst();
            if (ep.isPresent()) {
                //更新名称和序号
                SeriesDTO se = ep.get();
                e.setName(se.getName());
                e.setOrderNum(se.getOrderNum());
                if(StringUtils.isBlank(se.getArticleId())){
                    e.setArticleId(null);
                }else{
                    e.setArticleId(se.getArticleId());
                }
            } else {
                deleteList.add(e);
            }
        });
        // 将新增的节点保存到数据库
        data.stream().filter(e -> oldSeriesDO.stream().noneMatch(o -> o.getSeriesId().equals(e.getSeriesId()))).forEach(k -> {
            SeriesDO newDO = new SeriesDO();
            newDO.setName(k.getName());
            newDO.setOrderNum(k.getOrderNum());
            newDO.setParentId(k.getParentId());
            newDO.setRootId(rootId);
            if(StringUtils.isNotBlank(k.getArticleId())){
                newDO.setArticleId(k.getArticleId());
            }
            seriesDao.save(newDO);
        });
        // 删除多余的节点
        deleteList.forEach(e -> seriesDao.delete(e));
    }

    @Override
    public List<SeriesDTO> listSeriesRootByParameter(SeriesQuery query) {
        if (query == null) {
            query = new SeriesQuery();
        }
        query.addQuery(Query.isNull("rootId"));
        List<SeriesDO> list = seriesDao.listSeriesByParameter(query);
        return converter.toDto(list, query, SeriesDTO.class);
    }

    @Override
    public long countSeriesRootByParameter(SeriesQuery query) {
        if (query == null) {
            query = new SeriesQuery();
        }
        query.addQuery(Query.isNull("rootId"));
        return seriesDao.countSeriesByParameter(query);
    }

    @Override
    public List<SeriesDTO> listSeriesBySeriesId(String seriesId) {
        SeriesDO param = seriesDao.getById(seriesId);
        String rootId = param.getRootId()==null ? param.getSeriesId() : param.getRootId();
        SeriesQuery query = new SeriesQuery();
        query.addQuery(Query.equals("rootId", rootId));
        List<SeriesDO> list = seriesDao.listSeriesByParameter(query);
        SeriesDO rootSeries = seriesDao.getById(rootId);
        list.add(0, rootSeries);
        List<SeriesDTO> seriesDTOs = converter.toDto(list, null, SeriesDTO.class);
        List<String> articleId = seriesDTOs.stream().map(SeriesDTO::getArticleId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // 设置关联文章的名称
        if (!articleId.isEmpty()) {
            ArticleQuery query2 = new ArticleQuery();
            query2.addQuery(Query.in("articleId", articleId));
            List<ArticleDO> articleList = articleDao.listArticleByParameter(query2);
            articleList.forEach(e -> {
                seriesDTOs.forEach(s -> {
                    if (e.getArticleId().equals(s.getArticleId())) {
                        s.setArticleName(e.getTitle());
                    }
                });
            });
        }
        List<SeriesDTO> result = new ArrayList<>();
        SeriesDTO root = seriesDTOs.stream().filter(e -> e.getRootId() == null).findFirst().get();
        root.setLevel(1);
        result.add(root);
        //节点排序
        sort(rootId, 2, seriesDTOs, result);
        return result;
    }

    private void sort(String rootId, int level, List<SeriesDTO> allData, List<SeriesDTO> result) {
        List<SeriesDTO> list = allData.stream().filter(e -> rootId.equals(e.getParentId()))
                .sorted(Comparator.comparingInt(SeriesDTO::getOrderNum)).collect(Collectors.toList());
        list.forEach(e -> {
            e.setLevel(level);
            result.add(e);
            sort(e.getSeriesId(), level + 1, allData, result);
        });
    }
}
