package studio.xiaoyun.security.crypto;

/**
 * 密码服务，定义了加密和解密相关的接口
 */
public interface CipherService {

    /**
     * 将密文转换为明文
     * @param cipherText 加密的数据
     * @return 未加密的数据
     */
    String decrypt(String cipherText);

    /**
     * 将明文加密为密文
     * @param plainText 未加密的数据
     * @return 加密后的数据
     */
    String encrypt(String plainText);

}
