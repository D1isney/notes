import com.pojo.UserD;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        //  拿到容器
        //  默认只能构建无参
        //
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//        User user = (User) context.getBean("user");
        UserD user1 = (UserD) context.getBean("userD");
//        System.out.println(user == user1);
//        user.show();
        user1.show();
    }
}
