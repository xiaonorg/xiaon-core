package studio.xiaoyun.web;

/**
 * 封装http响应中的异常信息
 */
public class HttpError {
	/**
	 * 异常信息
	 */
	private String message;
	/**
	 * 错误码
	 */
	private ErrorCode code;

	public ErrorCode getCode() {
		return code;
	}

	public void setCode(ErrorCode code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	

}
