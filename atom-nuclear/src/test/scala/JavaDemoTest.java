import org.atom.Application;
import org.atom.service.DemoJavaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class JavaDemoTest {
    @Autowired
    private DemoJavaService demoJavaService;

    @Test
    public void demo1() {
        demoJavaService.detect();
    }
}
