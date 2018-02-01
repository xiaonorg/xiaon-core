package studio.xiaoyun.core.service;

import java.util.List;

/**
 * 意见反馈
 * @author 岳正灵
 *
 */
public interface FeedbackService {
	
	/**
	 * 创建意见反馈
	 * @param title 标题
	 * @param text 文字
	 * @return ID
	 * @since 1.0.0
	 */
	String saveFeedback(String title, String text);

}
