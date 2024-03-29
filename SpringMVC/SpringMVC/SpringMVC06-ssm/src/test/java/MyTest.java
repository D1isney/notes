import com.pojo.Books;
import com.service.BookServiceImpl;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class MyTest {

    @Test
    public void test() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        BookServiceImpl bookServiceImpl = context.getBean("BookServiceImpl", BookServiceImpl.class);
        BookServiceImpl bean = context.getBean("BookServiceImpl", BookServiceImpl.class);

        List<Books> books = bean.queryAllBook();
        for (Books book : books) {
            System.out.println(book);
        }
    }
}
