package studio.xiaoyun.core.dao;

import studio.xiaoyun.core.pojo.FeedbackDO;
import studio.xiaoyun.core.query.FeedbackQuery;

import java.util.List;

/**
 * 意见反馈
 */
public interface FeedbackDao extends AbstractDao<FeedbackDO> {
	
	/**
	 * 根据参数获得意见反馈的数量
	 * @param feedbackParameter 参数
	 * @return 数量
	 * @since 1.0.0
	 */
	long countFeedbackByParameter(FeedbackQuery feedbackParameter);
	
	/**
	 * 根据参数获得意见反馈的列表
	 * @param feedbackParameter 参数
	 * @return 意见反馈的列表
	 * @since 1.0.0
	 */
	List<FeedbackDO> listFeedbackByParameter(FeedbackQuery feedbackParameter);

	/**
	 * 根据id删除意见反馈
	 * @param feedbackIds 反馈的id
     */
	void deleteFeedback(List<String> feedbackIds);

}
