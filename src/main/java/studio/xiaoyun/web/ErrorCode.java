package studio.xiaoyun.web;

/**
 * 异常码
 * @author 岳正灵
 */
public enum ErrorCode {
	/**
	 * 服务器内部错误
	 */
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
	/**
	 * 请求参数错误
	 */
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST),
	/**
	 * 无权限
	 */
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
	/**
	 * 未知的账户
	 */
	UNKNOWN_ACCOUNT(HttpStatus.BAD_REQUEST),
	/**
	 * 无效的密码
	 */
	INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST),
	/**
	 * 账户不可用
	 */
	DISABLED_ACCOUNT(HttpStatus.BAD_REQUEST);

	private HttpStatus statusCode;

	/**
	 *
	 * @param statusCode http状态码
     */
	ErrorCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * 取得HTTP状态码
	 * @return HTTP状态码
	 * @since 1.0.0
	 */
	public HttpStatus getStatusCode() {
		return statusCode;
	}
}
