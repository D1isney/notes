import com.pojo.Hello;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    @Test
    public void demoTest() {
        //  获取Spring的上下文对象
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        //  对象都在Spring中管理了，使用就直接去里面取出来皆可以了
        Hello hello = (Hello) context.getBean("hello");
        hello.setName("DisneyD33");
        hello.show();
    }
}
