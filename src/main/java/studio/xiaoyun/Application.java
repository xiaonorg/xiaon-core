package studio.xiaoyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 整个应用的入口类
 */
@EnableScheduling  //支持定时任务
@SpringBootApplication  //表示整个应用的入口
@ServletComponentScan  //扫描WebFilter,WebServlet,WebListener注解
@EnableTransactionManagement   // 支持数据库事务
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}
