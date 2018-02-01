package studio.xiaoyun.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.nio.file.Files;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SystemConfigTest {
    @Resource
    private SystemConfig config;
    /**
     * 测试应用根目录
     */
    @Test
    public void testBasePath() {
        assertTrue(Files.exists(config.getBasePath()));
    }

    /**
     * 测试上传文件路径
     */
    @Test
    public void testUploadPath() {
        assertTrue(Files.exists(config.getFilePath()));
    }

    /**
     * 测试允许的上传文件的扩展名
     */
    @Test
    public void testAllowedExtension() {
        assertNotNull(config.getAllowedExtension());
    }


}