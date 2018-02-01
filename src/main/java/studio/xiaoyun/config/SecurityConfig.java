package studio.xiaoyun.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import studio.xiaoyun.core.exception.XyException;
import studio.xiaoyun.security.crypto.CipherService;
import studio.xiaoyun.security.crypto.RSACipherService;

import javax.annotation.Resource;

/**
 * 安全相关的配置
 */
@Configuration
public class SecurityConfig {
    @Value("${xiaoyun.security.cipherServiceClassName}")
    private String cipherServiceClassName;
    @Resource
    private Environment env;

    /**
     * @return 提供加解密功能的类
     */
    @Bean
    public CipherService cipherService() {
        CipherService cipherService;
        try {
            Class clazz = Class.forName(cipherServiceClassName);
            cipherService = (CipherService) clazz.newInstance();
            if (cipherService instanceof RSACipherService) {
                String encryptKey = env.getProperty("xiaoyun.security.encryptKey");
                if (StringUtils.isBlank(encryptKey)) {
                    throw new XyException("加密类为RSACipherService时，必须指定配置项xiaoyun.security.encryptKey");
                }
                ((RSACipherService) cipherService).setEncryptKey(encryptKey);
            }
        } catch (XyException e) {
            throw e;
        } catch (Exception e) {
            throw new XyException("创建CipherService时失败," + e.getMessage(), e);
        }
        return cipherService;
    }

}
