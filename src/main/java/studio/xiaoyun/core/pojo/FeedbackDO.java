package studio.xiaoyun.core.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 意见反馈
 * @author 岳正灵
 * @version 1.0.0
 */
@Entity
@Table(name = "xy_feedback")
public class FeedbackDO {

	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "studio.xiaoyun.core.pojo.UuidIdentifierGenerator")
	@Column(length = 32,name="feedbackid")
	private String feedbackId;
	/**
	 * 文字
	 */
	@Column(length = 255, nullable = false)
	private String text;
	/**
	 * 创建日期
	 */
	@Column(nullable = false,name="createdate")
	private LocalDateTime createDate ;
	
	public LocalDateTime getCreateDate() {
		return createDate;
	}


	public String getText() {
		return text;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}


	public void setText(String text) {
		this.text = text;
	}

	public String getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(String feedbackId) {
		this.feedbackId = feedbackId;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("FeedbackDO{");
		sb.append("feedbackId='").append(feedbackId).append('\'');
		sb.append(", text='").append(text).append('\'');
		sb.append(", createDate=").append(createDate);
		sb.append('}');
		return sb.toString();
	}
}
