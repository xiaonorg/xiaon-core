package studio.xiaoyun.core.query;

import java.time.LocalDateTime;

public class FeedbackQuery extends AbstractQuery {
	private static final long serialVersionUID = 8843191548287363823L;
	/**
	 * ID
	 */
	private String feedbackId;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 文字
	 */
	private String text;
	/**
	 * 创建日期
	 */
	private LocalDateTime createDate;
	
	public LocalDateTime getCreateDate() {
		return createDate;
	}
	public String getFeedbackId() {
		return feedbackId;
	}
	public String getText() {
		return text;
	}
	public String getTitle() {
		return title;
	}
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
	public void setFeedbackId(String feedbackId) {
		this.feedbackId= feedbackId;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
