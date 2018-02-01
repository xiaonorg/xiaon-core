package studio.xiaoyun.web;

/**
 * HTTP状态码
 * @author 岳正灵
 * @since 1.0.0
 */
public enum HttpStatus {
	/**
	 * 100 Continue/继续。
	 * <p>客户端应当继续发送请求。这个临时响应是用来通知客户端它的部分请求已经被服务器接收，
	 * 且仍未被拒绝。客户端应当继续发送请求的剩余部分，或者如果请求已经完成，忽略这个响应。
	 * 服务器必须在请求完成后向客户端发送一个最终响应。
	 */
	CONTINUE(100),
	/**
	 * 101 Switching Protocols/转换协议。
	 * <p>服务器已经理解了客户端的请求，并将通过Upgrade 消息头通知客户端采用不同的协议来完成这个请求。
	 * 在发送完这个响应最后的空行后，服务器将会切换到在Upgrade 消息头中定义的那些协议。
	 */
	SWITCHING_PROTOCOLS(101),
	/**
	 * 200 OK/正常。
	 * <p>请求已成功，请求所希望的响应头或数据体将随此响应返回。
	 */
	OK(200),
	/**
	 * 201 Created/已创建。
	 * <p>请求已经被实现，而且有一个新的资源已经依据请求的需要而建立，且其 URI 已经随Location 头信息返回。
	 * 假如需要的资源无法及时建立的话，应当返回 '202 Accepted'。
	 */
	CREATED(201),
	/**
	 * 202 Accepted/已接受。
	 * <p>服务器已接受请求，但尚未处理。正如它可能被拒绝一样，最终该请求可能会也可能不会被执行。
	 * 在异步操作的场合下，没有比发送这个状态码更方便的做法了。
	 */
	ACCEPTED(202),
	/**
	 * 204 No Content/无响应体。
	 * <p>服务器成功处理了请求，但不需要返回任何实体内容。
	 */
	NO_CONTENT(204),
	/**
	 * 205 Reset Content/无响应体。
	 * <p>服务器成功处理了请求，且没有返回任何内容。但是与204响应不同，返回此状态码的响应要求请求者重置文档视图。
	 * 该响应主要是被用于接受用户输入后，立即重置表单，以便用户能够轻松地开始另一次输入。
	 */
	RESET_CONTENT(205),
	/**
	 * 206 Partial Content。
	 * <p>服务器已经成功处理了部分 GET 请求。类似于 FlashGet 或者迅雷这类的 HTTP下载工具都是使用此类响应实现
	 * 断点续传或者将一个大文档分解为多个下载段同时下载。该请求必须包含 Range 头信息来指示客户端希望得到的内容范围，
	 * 并且可能包含 If-Range 来作为请求条件。
	 */
	PARTIAL_CONTENT(206),
	/**
	 * 300 Multiple Choices。
	 * <p>被请求的资源有一系列可供选择的回馈信息，每个都有自己特定的地址和浏览器驱动的商议信息。
	 * 用户或浏览器能够自行选择一个首选的地址进行重定向。
	 */
	MULTIPLE_CHOICES(300),
	/**
	 * 301 Moved Permanently。
	 * <p>被请求的资源已永久移动到新位置，并且将来任何对此资源的引用都应该使用本响应返回的若干个 URI 之一。
	 * 如果可能，拥有链接编辑功能的客户端应当自动把请求的地址修改为从服务器反馈回来的地址。
	 */
	MOVED_PERMANENTLY(301),
	/**
	 * 304 Not Modified/未改变。
	 * <p>如果客户端发送了一个带条件的 GET 请求且该请求已被允许，而文档的内容（自上次访问以来或者根据请求的条件）并没有改变，
	 * 则服务器应当返回这个状态码。304响应禁止包含消息体，因此始终以消息头后的第一个空行结尾。
	 */
	NOT_MODIFIED(304),
	/**
	 * 400 Bad Request/无效的请求。
	 * <p>1、语义有误，当前请求无法被服务器理解。2、请求参数有误。
	 */
	BAD_REQUEST(400),
	/**
	 * 401 Unauthorized/无权限。
	 * <p>当前请求需要用户验证,或者请求没有通过验证
	 */
	UNAUTHORIZED(401),
	/**
	 * 403 Forbidden/拒绝执行。
	 * <p>服务器已经理解请求，但是拒绝执行它。
	 */
	FORBIDDEN(403),
	/**
	 * 404 Not Found/未发现资源。
	 * <p>请求失败，请求所希望得到的资源未在服务器上发现。
	 * 404这个状态码被广泛应用于当服务器不想揭示到底为何请求被拒绝或者没有其他适合的响应可用的情况下。
	 */
	NOT_FOUND(404),
	/**
	 * 405 Method Not Allowed/不支持的方法。
	 * <p>请求行中指定的请求方法不能被用于请求相应的资源。该响应必须返回一个Allow 头信息用以表示出当前资源能够接受的请求方法的列表。
	 */
	METHOD_NOT_ALLOWED(405),
	/**
	 * 406 Not Acceptable。
	 * <p>请求的资源的内容特性无法满足请求头中的条件，因而无法生成响应实体。
	 */
	NOT_ACCEPTABLE(406),
	/**
	 * 408 Request Timeout/请求超时。
	 */
	REQUEST_TIMEOUT(408),
	/**
	 * 411 Length Required。
	 * <p>服务器拒绝在没有定义 Content-Length头的情况下接受请求。
	 */
	LENGTH_REQUIRED(411),
	/**
	 * 413 Payload Too Large。
	 * <p>服务器拒绝处理当前请求，因为该请求提交的实体数据大小超过了服务器愿意或者能够处理的范围。
	 */
	PAYLOAD_TOO_LARGE(413),
	/**
	 * 415 Unsupported Media Type/不支持的媒体类型。
	 * <p>对于当前请求的方法和所请求的资源，请求中提交的实体并不是服务器中所支持的格式，因此请求被拒绝。
	 */
	UNSUPPORTED_MEDIA_TYPE(415),
	/**
	 * 500 Internal Server Error/服务器内部错误。
	 * <p>服务器遇到了一个未曾预料的状况，导致了它无法完成对请求的处理。
	 */
	INTERNAL_SERVER_ERROR(500),
	/**
	 * 501 Not Implemented。
	 * <p>服务器不支持当前请求所需要的某个功能。当服务器无法识别请求的方法，并且无法支持其对任何资源的请求。
	 */
	NOT_IMPLEMENTED(501);

	private final int value;

	private HttpStatus(int value) {
		this.value = value;
	}

	/**
	 * 数字格式的状态码
	 * @return 状态码
	 */
	public int value() {
		return this.value;
	}

}
