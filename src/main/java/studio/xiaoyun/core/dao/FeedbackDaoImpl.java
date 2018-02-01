package studio.xiaoyun.core.dao;

import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import studio.xiaoyun.core.pojo.FeedbackDO;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.query.FeedbackQuery;

import java.util.List;

@Repository("feedbackDao")
public class FeedbackDaoImpl extends AbstractDaoImpl<FeedbackDO> implements FeedbackDao {

    @Override
    public FeedbackDO getById(String id) throws InvalidParameterException {
        FeedbackDO feedback = getSession().get(FeedbackDO.class, id);
        if(feedback==null){
            throw new InvalidParameterException(id+"不存在");
        }
        return feedback;
    }

    @Override
    public FeedbackDO loadById(String id) {
        return getSession().load(FeedbackDO.class, id);
    }

    @Override
    public long countFeedbackByParameter(FeedbackQuery parameter) {
        return countByParameter(null,null,parameter);
    }

    @Override
    public List<FeedbackDO> listFeedbackByParameter(FeedbackQuery parameter) {
        return listByParameter(null,null,parameter,FeedbackDO.class);
    }

    @Override
    public void deleteFeedback(List<String> feedbackIds) {
        String sql = "delete from xy_feedback where feedbackId in (:feedbackId)";
        NativeQuery query = getSession().createNativeQuery(sql);
        query.setParameter("feedbackId",feedbackIds);
        query.executeUpdate();
    }

    @Override
    String getQuerySql() {
        return "select feedback_0.* from xy_feedback as feedback_0";
    }

}
