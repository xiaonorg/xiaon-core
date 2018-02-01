package studio.xiaoyun.web.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import studio.xiaoyun.core.dao.SeriesDao;
import studio.xiaoyun.core.dto.DTOConverter;
import studio.xiaoyun.core.dto.SeriesDTO;
import studio.xiaoyun.core.pojo.SeriesDO;
import studio.xiaoyun.core.query.SeriesQuery;
import studio.xiaoyun.core.service.SeriesService;
import studio.xiaoyun.security.annotation.RequireUser;
import studio.xiaoyun.web.ParameterUtil;
import studio.xiaoyun.web.WebResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class SeriesController {
    @Resource
    private SeriesDao seriesDao;
    @Resource
    private SeriesService seriesService;
    @Resource
    private DTOConverter converter;

    /**
     * 创建一个系列
     */
    @RequireUser
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/series", method = RequestMethod.PUT)
    public SeriesDTO createSeries(String name) {
        String seriesId = seriesService.createSeries(name);
        SeriesDO seriesDO = seriesDao.getById(seriesId);
        return converter.toDto(seriesDO, null, SeriesDTO.class);
    }

    @RequireUser
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/series/{Id:\\S{32}}", method = RequestMethod.DELETE)
    public void deleteSeries(@PathVariable("Id") String seriesId) {
        seriesService.deleteSeries(seriesId);
    }

    /**
     * 更新数据
     */
    @RequireUser
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/series/{Id:\\S{32}}", method = RequestMethod.POST)
    public void updateSeries(@PathVariable("Id") String seriesId, String param) {
        List<SeriesDTO> list = JSON.parseArray(param, SeriesDTO.class);
        seriesService.updateSeries(seriesId, list);
    }

    @RequireUser
    @RequestMapping(value = "/series", method = RequestMethod.GET)
    public WebResult getSeriesRootByParameter(HttpServletRequest request) {
        SeriesQuery param = ParameterUtil.getParameter(request, SeriesQuery.class);
        long count = seriesService.countSeriesRootByParameter(param);
        List<SeriesDTO> list = seriesService.listSeriesRootByParameter(param);
        return new WebResult(count, list);
    }

    /**
     * 根据系列的id获得整个树
     * @param seriesId 任意节点的id
     * @return 系列的所有节点信息
     */
    @RequestMapping(value = "/series/{Id:\\S{32}}/all", method = RequestMethod.GET)
    public WebResult listSeriesBySeriesId(@PathVariable("Id") String seriesId) {
        List<SeriesDTO> list = seriesService.listSeriesBySeriesId(seriesId);
        return new WebResult(list.size(), list);
    }

}
