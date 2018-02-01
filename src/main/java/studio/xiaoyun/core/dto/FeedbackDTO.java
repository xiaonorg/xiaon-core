package studio.xiaoyun.core.dto;

import java.time.LocalDateTime;

/**
 * 意见反馈
 * @author 岳正灵
 */
public class FeedbackDTO implements AbstractDTO {
	private String feedbackId;
	private String title;
	private String text;
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
		this.feedbackId = feedbackId;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("FeedbackDTO{");
		sb.append("feedbackId='").append(feedbackId).append('\'');
		sb.append(", title='").append(title).append('\'');
		sb.append(", text='").append(text).append('\'');
		sb.append(", createDate=").append(createDate);
		sb.append('}');
		return sb.toString();
	}
}
