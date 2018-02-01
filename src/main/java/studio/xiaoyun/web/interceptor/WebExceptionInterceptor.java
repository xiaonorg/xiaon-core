package studio.xiaoyun.web.interceptor;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import studio.xiaoyun.core.exception.InvalidParameterException;
import studio.xiaoyun.core.exception.XyException;
import studio.xiaoyun.web.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通过web方式访问时，所有未捕获的异常都会在这里处理
 * @author 岳正灵
 * @since 1.0.0
 *
 */
public class WebExceptionInterceptor implements HandlerExceptionResolver {
    private Logger logger = LoggerFactory.getLogger(WebExceptionInterceptor.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) {
		String message = "未知错误";
		int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		if(exception instanceof AuthorizationException){    //安全相关的异常
			message = "无权限";
			statusCode = HttpStatus.UNAUTHORIZED.value();
			errorCode = ErrorCode.UNAUTHORIZED;
			logger.warn("用户试图访问一个资源，但是没有权限。url:{} ,ip:{}",request.getRequestURL(),request.getRemoteAddr());
		}else if(exception instanceof InvalidParameterException){   //参数错误
			statusCode = HttpStatus.BAD_REQUEST.value();
			errorCode = ErrorCode.INVALID_PARAMETER;
			message = exception.getMessage()==null?"null":exception.getMessage();
			logger.info(message,exception);
		}else if(exception instanceof WebException){    //web模块的异常
			WebException webException = (WebException)exception;
			statusCode = webException.getErrorCode().getStatusCode().value();
			if(!ErrorCode.INTERNAL_SERVER_ERROR.equals(webException.getErrorCode())){
				errorCode = webException.getErrorCode();
				message = exception.getMessage()==null?"null":exception.getMessage();
			}
			logger.warn(message,exception);
		}else if(exception instanceof XyException){    //其它自定义异常
			message = exception.getMessage()==null?"null":exception.getMessage();
			logger.warn(message,exception);
		}else{          //所有未知异常
			logger.error(message, exception);
		}

		//封装错误信息
		HttpError httpError = new HttpError();
		httpError.setMessage(message);
		httpError.setCode(errorCode);

		response.setStatus(statusCode);    //设置响应的状态码
		ModelAndView result = new ModelAndView();
		result.addObject("error", httpError);      //以json、xml等格式返回错误信息
		return result;
	}

}
