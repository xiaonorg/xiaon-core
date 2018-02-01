package studio.xiaoyun.security.auth;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.stereotype.Component;
import studio.xiaoyun.security.crypto.CipherService;

import javax.annotation.Resource;

/**
 * 凭证匹配器，验证凭证是否正确
 */
@Component
public class CredentialsMatcher implements org.apache.shiro.authc.credential.CredentialsMatcher {
    @Resource
    private CipherService cipherService;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String storedPassword = (String) info.getCredentials();    //数据库中存储的密码
        String userPassword = new String((char[])token.getCredentials());   //用户输入的密码
        return storedPassword.equals(cipherService.encrypt(userPassword));
    }

}
