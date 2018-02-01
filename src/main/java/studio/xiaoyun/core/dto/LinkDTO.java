package studio.xiaoyun.core.dto;

public class LinkDTO implements AbstractDTO {
	private String source;
	private String target;
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("LinkDTO{");
		sb.append("source='").append(source).append('\'');
		sb.append(", target='").append(target).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
