package studio.xiaoyun.app;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

public class InstantTest {

    @Test
    public void test() {
        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        String str1 = time1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String str2 = time2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        assertEquals(str1, str2);
    }
}
