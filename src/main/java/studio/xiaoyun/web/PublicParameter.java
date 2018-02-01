package studio.xiaoyun.web;

/**
 * 定义HTTP请求中通用的请求参数
 */
public enum PublicParameter {
	/**
	 * 开始记录，从0开始的整数
	 */
	START("start"), 
	/**
	 * 页数，从1开始的整数
	 */
	PAGE("page"), 
	/**
	 * 最多返回记录的数量，从1开始的整数
	 */
	ROWS("rows"), 
	/**
	 * 返回结果中应该包含的字段,多个字段之间以逗号分隔
	 */
	FIELD("field"), 
	/**
	 * HTTP方法,get、post、delete、put
	 */
	METHOD("method"), 
	/**
	 * 媒体类型，html、json、xml
	 */
	FORMAT("format"), 
	/**
	 * 搜索条件
	 */
	SEARCH("search"),
	/**
	 * 是否将搜索条件进行urlencode编码
	 */
	SEARCH_ENCODE("search_encode"),
	/**
	 * 排序条件
	 */
	SORT("sort"),
	/**
	 * jsonp请求时使用该参数指定回调函数
	 */
	CALLBACK("callback");

	private final String name;

	PublicParameter(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return 通用参数的名称
	 */
	public String value() {
		return this.name;
	}

}
