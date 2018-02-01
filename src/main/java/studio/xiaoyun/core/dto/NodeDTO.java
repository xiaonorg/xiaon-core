package studio.xiaoyun.core.dto;

public class NodeDTO implements AbstractDTO {
	private String id;
	private String title;
	private String url;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("NodeDTO{");
		sb.append("id='").append(id).append('\'');
		sb.append(", title='").append(title).append('\'');
		sb.append(", url='").append(url).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
