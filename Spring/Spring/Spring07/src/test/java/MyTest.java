import com.config.UserConfig;
import com.pojo.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        //  如果完全使用配置类方式去做，只能通过AnnotationConfigApplicationContext 上下文来获取容器，通过配置类的class对象加载
        ApplicationContext context = new AnnotationConfigApplicationContext(UserConfig.class);
        User myUser = context.getBean("myUser", User.class);
        System.out.println(myUser);
    }
}
