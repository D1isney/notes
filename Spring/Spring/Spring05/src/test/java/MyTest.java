import com.pojo.People;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    @Test
    public void test1(){
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        People people = context.getBean("people", People.class);
        people.getDog().shot();
        people.getCat().shot();
    }
}
