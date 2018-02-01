package studio.xiaoyun.core.dao;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.pojo.SeriesDO;
import studio.xiaoyun.core.query.SeriesQuery;

import java.util.List;

@Repository("seriesDao")
public class SeriesDaoImpl extends AbstractDaoImpl<SeriesDO> implements SeriesDao {
    @Override
    public SeriesDO getById(String id) throws InvalidParameterException {
        SeriesDO data = getSession().get(SeriesDO.class, id);
        if (data == null) {
            throw new InvalidParameterException(id + "不存在");
        }
        return data;
    }

    @Override
    public SeriesDO loadById(String id) {
        return getSession().load(SeriesDO.class, id);
    }

    @Override
    public List<SeriesDO> listSeriesByParameter(SeriesQuery param) {
        return listByParameter(null, null, param, SeriesDO.class);
    }

    @Override
    public long countSeriesByParameter(SeriesQuery param) {
        return countByParameter(null, null, param);
    }

    @Override
    public void deleteSeriesByRootId(String rootId) {
        //删除子节点
        String sql = "delete from xy_series where rootid=?";
        Query query = getSession().createNativeQuery(sql);
        query.setParameter(1,rootId);
        query.executeUpdate();
        //删除根节点
        delete(rootId);
    }

    @Override
    String getQuerySql() {
        return "select series_0.* from xy_series as series_0";
    }
}
