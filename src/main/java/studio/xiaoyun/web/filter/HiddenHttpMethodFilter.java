package studio.xiaoyun.web.filter;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import studio.xiaoyun.web.PublicParameter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * HTTP方法过滤器，根据HTTP请求的参数，将POST类型的HTTP请求修改为参数的值。
 * <p>为了安全考虑，不允许将GET类型的请求修改为其它类型的请求;
 * 为了不和{@linkplain HttpFormContentFilter HttpFormContentFilter}冲突，
 * 不支持将PUT和DELETE类型的请求修改为其它类型的请求</p>
 */
@WebFilter(filterName="hiddenHttpMethodFilter",urlPatterns="/*")
public class HiddenHttpMethodFilter implements Filter {

	@Override
	public void destroy(){}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		ServletRequest request2 = request;
		// 如果是http请求，并且不是在上传文件
		if(request instanceof HttpServletRequest && !ServletFileUpload.isMultipartContent((HttpServletRequest)request)){
			String method = request.getParameter(PublicParameter.METHOD.value());
			String requestMethod = ((HttpServletRequest) request).getMethod();
			if(method!=null && "POST".equalsIgnoreCase(requestMethod)
					&& method.toUpperCase().matches("GET|PUT|POST|DELETE")){
				request2 = new HttpMethodRequestWrapper((HttpServletRequest)request,method.toUpperCase());
			}
		}
		filterChain.doFilter(request2, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {}
	
	private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {

		private final String method;

		HttpMethodRequestWrapper(HttpServletRequest request, String method) {
			super(request);
			this.method = method;
		}

		@Override
		public String getMethod() {
			return this.method;
		}
	}

}
