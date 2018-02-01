package studio.xiaoyun.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import studio.xiaoyun.core.exception.XyException;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 封装druid相关的配置信息，并使用druid创建一个DataSource。
 * <p>关于druid更多的信息见<a href="https://github.com/alibaba/druid/wiki">https://github.com/alibaba/druid/wiki</a></p>
 */
@Configuration
public class DruidConfig {
    /**
     * 访问数据库的url
     */
    @Value("${xiaoyun.datasource.url}")
    private String dbUrl;
    /**
     * 用户名
     */
    @Value("${xiaoyun.datasource.username}")
    private String username;
    /**
     * 密码
     */
    @Value("${xiaoyun.datasource.password}")
    private String password;
    /**
     * 驱动类
     */
    @Value("${xiaoyun.datasource.driverClassName}")
    private String driverClassName;
    /**
     * 初始化连接数
     */
    @Value("${xiaoyun.datasource.initialSize:0}")
    private int initialSize;
    /**
     * 最小连接数
     */
    @Value("${xiaoyun.datasource.minIdle:0}")
    private int minIdle;
    /**
     * 最大连接数
     */
    @Value("${xiaoyun.datasource.maxActive:8}")
    private int maxActive;
    /**
     * 获取连接时最大的等待时间, 单位是毫秒
     */
    @Value("${xiaoyun.datasource.maxWait:30000}")
    private int maxWait;
    /**
     * 间隔多久检测一次连接是否空闲，如果连接空闲时间大于等于minEvictableIdleTimeMillis则关闭物理连接，单位是毫秒
     */
    @Value("${xiaoyun.datasource.timeBetweenEvictionRunsMillis:60000}")
    private int timeBetweenEvictionRunsMillis;
    /**
     * 连接保持空闲而不被驱逐的最长时间，单位是毫秒，默认5小时
     */
    @Value("${xiaoyun.datasource.minEvictableIdleTimeMillis:18000000}")
    private int minEvictableIdleTimeMillis;
    /**
     * 用来检测连接是否有效的sql，要求是一个查询语句
     */
    @Value("${xiaoyun.datasource.validationQuery:select 'x'}")
    private String validationQuery;
    /**
     * 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，
     *  执行validationQuery检测连接是否有效
     */
    @Value("${xiaoyun.datasource.testWhileIdle:true}")
    private boolean testWhileIdle;
    /**
     * 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭
     */
    @Value("${xiaoyun.datasource.poolPreparedStatements:false}")
    private boolean poolPreparedStatements;
    /**
     * 要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。
     * 在Druid中，不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100
     */
    @Value("${xiaoyun.datasource.maxPoolPreparedStatementPerConnectionSize:-1}")
    private int maxPoolPreparedStatementPerConnectionSize;
    /**
     * 监控统计用的stat 日志用的log4j 防御sql注入的wall
     */
    @Value("${xiaoyun.datasource.filters:stat}")
    private String filters;

    @Bean
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try{
            datasource.setFilters(filters);
        }catch(SQLException e){
            throw new XyException("设置druid的插件时出错，"+e.getMessage(),e);
        }
        return datasource;
    }
}