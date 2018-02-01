package studio.xiaoyun.core.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

public class ArticleServiceImplTest {
    @Test
    public void formatHtml() throws Exception {
        String html = "<h1>aa</h1><p>bb</p>";
        ArticleServiceImpl service = new ArticleServiceImpl();
        String text = service.formatHtml(html);
        text = text.replaceAll("\\s","");
        assertTrue(text.contains("<divclass=\"md-h1\"><fieldset><legend>aa</legend></fieldset></div><p>bb</p>"));
    }

    @Test
    public void parserText() throws Exception {
        String html = "<div>aa<code>bb</code>cc</div>";
        ArticleServiceImpl service = new ArticleServiceImpl();
        String text = service.parserText(html);
        assertEquals("aacc", text);
    }

}