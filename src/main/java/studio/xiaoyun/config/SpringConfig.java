package studio.xiaoyun.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import org.hibernate.SessionFactory;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import reactor.Environment;
import reactor.bus.EventBus;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;

/**
 * spring相关的配置
 */
@Configuration
public class SpringConfig {

    /**
     * 控制器返回对象时使用fastjson将对象转换为json字符串
     * @return   fastjson的转换器
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializeConfig(getFastJsonSerializeConfig());
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastConverter);
    }

    /**
     * 这个bean的功能是根据请求自动选择合适视图解析器，json、xml等
     * @return ContentNegotiatingViewResolver的实例
     */
    @Bean
    public ContentNegotiatingViewResolver contentNegotiatingViewResolver() {
        ContentNegotiationManagerFactoryBean managerFactoryBean = new ContentNegotiationManagerFactoryBean();
        //添加支持的媒体类型
        managerFactoryBean.addMediaType("json", MediaType.APPLICATION_JSON);
        managerFactoryBean.setDefaultContentType(MediaType.APPLICATION_JSON);
        //是否忽略请求头中的accept
        managerFactoryBean.setIgnoreAcceptHeader(false);
        //是否使用url中的扩展名表示媒体类型，例如/v1/user.json
        managerFactoryBean.setFavorPathExtension(true);
        //是否忽略未知的扩展名
        managerFactoryBean.setIgnoreUnknownPathExtensions(true);

        //fastjson相关的配置
        FastJsonJsonView fastJsonJsonView = new FastJsonJsonView();
        fastJsonJsonView.setExtractValueFromSingleKeyModel(true);
        fastJsonJsonView.setContentType("application/json;charset=UTF-8");
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializeConfig(getFastJsonSerializeConfig());
        fastJsonJsonView.setFastJsonConfig(fastJsonConfig);

        ContentNegotiatingViewResolver contentNegotiatingViewResolver = new ContentNegotiatingViewResolver();
        contentNegotiatingViewResolver.setContentNegotiationManager(managerFactoryBean.getObject());
        contentNegotiatingViewResolver.setOrder(0);
        contentNegotiatingViewResolver.setDefaultViews(Collections.singletonList(fastJsonJsonView));
        return contentNegotiatingViewResolver;
    }

    /**
     * 创建hibernate的事务管理器
     * @param entityManagerFactory 自动创建的jpa实体管理器
     * @return hibernate的事务管理器
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        hibernateTransactionManager.setSessionFactory(sessionFactory);
        return hibernateTransactionManager;
    }

    /**
     * 事务相关的拦截器。
     * <p>实现执行某些方法时打开数据库事务，方法执行完后自动提交事务</p>
     * @param ptm 事务管理器
     * @return 事务相关的拦截器
     */
    @Bean("transactionMethodInterceptor")
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager ptm) {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        RuleBasedTransactionAttribute attribute = new RuleBasedTransactionAttribute();
        attribute.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        attribute.setReadOnly(true);
        source.addTransactionalMethod("get*", attribute);
        source.addTransactionalMethod("list*", attribute);
        source.addTransactionalMethod("count*", attribute);

        RuleBasedTransactionAttribute attribute2 = new RuleBasedTransactionAttribute();
        attribute2.setPropagationBehaviorName("PROPAGATION_REQUIRED");
        //抛出任意异常时都回滚事务
        attribute2.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Throwable.class)));
        source.addTransactionalMethod("save*", attribute2);
        source.addTransactionalMethod("create*", attribute2);
        source.addTransactionalMethod("update*", attribute2);
        source.addTransactionalMethod("delete*", attribute2);
        source.addTransactionalMethod("set*", attribute2);
        return new TransactionInterceptor(ptm, source);
    }

    /**
     * 设置哪些类需要事务支持
     * @return 一个事务切面
     */
    @Bean("transactionProxy")
    public AbstractAutoProxyCreator transactionProxy() {
        BeanNameAutoProxyCreator proxy = new BeanNameAutoProxyCreator(){
            @Override
            protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource targetSource) {
                Object[] val = super.getAdvicesAndAdvisorsForBean(beanClass,beanName,targetSource);
                String name = beanClass.getName();
                if(val==PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS){
                    //设置只有dao和service包下的类才支持事务
                    if(!name.startsWith("studio.xiaoyun.core.dao") && !name.startsWith("studio.xiaoyun.core.service")){
                        val = DO_NOT_PROXY;
                    }
                }
               return val;
            }
        };
        proxy.setBeanNames("*Dao","*Service");
        proxy.setInterceptorNames("transactionMethodInterceptor");
        return proxy;
    }

    /**
     * @return 事件驱动相关的bean
     */
    @Bean(name = "eventBus")
    public EventBus createEventBus() {
        Environment environment = Environment.initializeIfEmpty().assignErrorJournal();
        return EventBus.create(environment);
    }

    /**
     * @return FastJson工具序列化时用到的配置信息
     */
    private static SerializeConfig getFastJsonSerializeConfig() {
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        serializeConfig.put(LocalDateTime.class, (serializer, object, fieldName, fieldType, features) -> {
            if (object == null) {
                serializer.out.writeNull();
            } else {
                LocalDateTime date = (LocalDateTime) object;
                serializer.write(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        });
        serializeConfig.put(LocalDate.class, (serializer, object, fieldName, fieldType, features) -> {
            if (object == null) {
                serializer.out.writeNull();
            } else {
                LocalDate date = (LocalDate) object;
                serializer.write(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });
        serializeConfig.put(LocalTime.class, (serializer, object, fieldName, fieldType, features) -> {
            if (object == null) {
                serializer.out.writeNull();
            } else {
                LocalTime date = (LocalTime) object;
                serializer.write(date.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        });
        return serializeConfig;
    }
}
