package studio.xiaoyun.security.crypto;

import com.alibaba.druid.filter.config.ConfigTools;
import studio.xiaoyun.core.exception.NotImplementedException;
import studio.xiaoyun.core.exception.XyException;

/**
 * RSA加密
 */
public class RSACipherService implements CipherService {
    private String encryptKey;

    @Override
    public String decrypt(String cipherText) {
        throw new NotImplementedException();
    }

    @Override
    public String encrypt(String plainText) {
        if (encryptKey == null) {
            throw new XyException("请设置key");
        }
        try {
            return ConfigTools.encrypt(encryptKey, plainText);
        } catch (Exception e) {
            throw new XyException("加密失败," + e.getMessage(), e);
        }
    }

    /**
     * 设置加密的key
     * @param encryptKey 加密的key
     */
    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }
}
