package studio.xiaoyun.core.service;

import studio.xiaoyun.core.dto.SeriesDTO;
import studio.xiaoyun.core.dto.SeriesTreeDTO;
import studio.xiaoyun.core.query.SeriesQuery;

import java.util.List;

public interface SeriesService {
    /**
     * 创建一个系列
     *
     * @param name 名称
     * @return 系统的id
     */
    String createSeries(String name);

    /**
     * 删除系列
     *
     * @param seriesId 系统的id
     */
    void deleteSeries(String seriesId);

    /**
     * 更新数据
     * @param rootId 根节点的id
     * @param data 系列中的所有数据
     */
    void updateSeries(String rootId,List<SeriesDTO> data);

    /**
     * 获得系列的根节点
     *
     * @param query 搜索参数
     * @return 列表
     */
    List<SeriesDTO> listSeriesRootByParameter(SeriesQuery query);

    /**
     * 获得系列的根节点的数量
     *
     * @param query 搜索参数
     * @return 数量
     */
    long countSeriesRootByParameter(SeriesQuery query);

    /**
     * 根据系列id获得整个系列的信息
     * @param seriesId 系列的任意节点的id
     * @return 整个系列的信息
     */
    List<SeriesDTO> listSeriesBySeriesId(String seriesId);
}
