package studio.xiaoyun.security.crypto;

/**
 * 简单的密码服务实现类，不做任何加密或者解密的处理
 */
public class SimpleCipherService implements CipherService{
    @Override
    public String decrypt(String data) {
        return data;
    }

    @Override
    public String encrypt(String data) {
        return data;
    }
}
