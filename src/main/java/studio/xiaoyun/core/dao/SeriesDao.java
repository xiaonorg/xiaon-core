package studio.xiaoyun.core.dao;

import studio.xiaoyun.core.pojo.SeriesDO;
import studio.xiaoyun.core.query.SeriesQuery;

import java.util.List;

public interface SeriesDao extends AbstractDao<SeriesDO> {
    /**
     * 获得文章系列信息
     *
     * @param param 搜索参数
     * @return 文章系列信息
     */
    List<SeriesDO> listSeriesByParameter(SeriesQuery param);

    /**
     * 文章系列的数量
     *
     * @param param 　搜索参数
     * @return 文章系列的数量
     */
    long countSeriesByParameter(SeriesQuery param);

    /**
     * 根据系列的根id删除所有节点
     * @param rootId 根id
     */
    void deleteSeriesByRootId(String rootId);
}
