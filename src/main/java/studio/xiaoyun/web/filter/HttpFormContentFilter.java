package studio.xiaoyun.web.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 默认情况下使用request.getParameter()等方法时无法获得PUT和DELETE请求体中的参数，只能获得url中的参数，该过滤器解决这个问题
 * @author 岳正灵
 * @since 1.0.0
 */
@WebFilter(filterName="httpFormContentFilter",urlPatterns="/*")
public class HttpFormContentFilter implements Filter {
	private final FormHttpMessageConverter formConverter = new AllEncompassingFormHttpMessageConverter();

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		ServletRequest request2 = request;
		// 如果是http请求，并且不是在上传文件
		if (request instanceof HttpServletRequest && !ServletFileUpload.isMultipartContent((HttpServletRequest)request)) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			if (("PUT".equals(httpRequest.getMethod()) || "DELETE".equals(httpRequest.getMethod()))) {
				HttpInputMessage inputMessage = new ServletServerHttpRequest(httpRequest) {
					@Override
					public InputStream getBody() throws IOException {
						return request.getInputStream();
					}
				};
				MultiValueMap<String, String> formParameters = formConverter.read(null, inputMessage);
				request2 = new HttpFormContentRequestWrapper(httpRequest, formParameters);
			}
		}
		filterChain.doFilter(request2, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {}

	private static class HttpFormContentRequestWrapper extends HttpServletRequestWrapper {

		private MultiValueMap<String, String> formParameters;

		HttpFormContentRequestWrapper(HttpServletRequest request, MultiValueMap<String, String> parameters) {
			super(request);
			this.formParameters = (parameters != null) ? parameters : new LinkedMultiValueMap<>();
		}

		@Override
		public String getParameter(String name) {
			String queryStringValue = super.getParameter(name);
			String formValue = this.formParameters.getFirst(name);
			return (queryStringValue != null) ? queryStringValue : formValue;
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			Map<String, String[]> result = new LinkedHashMap<>();
			Enumeration<String> names = this.getParameterNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				result.put(name, this.getParameterValues(name));
			}
			return result;
		}

		@Override
		public Enumeration<String> getParameterNames() {
			Set<String> names = new LinkedHashSet<>();
			names.addAll(Collections.list(super.getParameterNames()));
			names.addAll(this.formParameters.keySet());
			return Collections.enumeration(names);
		}

		@Override
		public String[] getParameterValues(String name) {
			String[] queryStringValues = super.getParameterValues(name);
			List<String> formValues = this.formParameters.get(name);
			if (formValues == null) {
				return queryStringValues;
			} else if (queryStringValues == null) {
				return formValues.toArray(new String[formValues.size()]);
			} else {
				List<String> result = new ArrayList<>();
				result.addAll(Arrays.asList(queryStringValues));
				result.addAll(formValues);
				return result.toArray(new String[result.size()]);
			}
		}
	}
}
