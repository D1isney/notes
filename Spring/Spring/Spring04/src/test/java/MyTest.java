import com.pojo.Student;
import com.pojo.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Student student = (Student) context.getBean("student");
        System.out.println(student);
        /*
        * Student{
        *   name='DisneyD33',
        *   address=Address{address='广州'},
        *   books=[JavaWeb, Web, Java],
        *   hobby=[Code, TV, Phone],
        *   card={ID=121700, pass=disney},
        *   game=[王者荣耀, 和平精英],
        *   wife='null',
        *   info={passWord=passWord, userName=userName}}
        * */

        User user = context.getBean("user",User.class);
        System.out.println(user);

        User user2 = context.getBean("user2",User.class);
        User user3 = context.getBean("user2",User.class);
//        System.out.println(user2);
        System.out.println(user2==user3);
    }
}
