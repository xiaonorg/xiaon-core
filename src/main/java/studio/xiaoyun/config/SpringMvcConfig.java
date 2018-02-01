package studio.xiaoyun.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import studio.xiaoyun.web.interceptor.WebExceptionInterceptor;

import java.util.List;

/**
 * spring mvc相关的配置
 */
@Configuration
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

    /**
     * 添加异常处理器，底层未处理的异常都会被异常处理器拦截
     * @param exceptionResolvers 异常处理器列表
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new WebExceptionInterceptor());
    }

}
