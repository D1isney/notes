import com.dao.UserDaoMySqlImpl;
import com.dao.UserDaoOracleImpl;
import com.service.UserService;
import com.service.UserServiceImpl;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserTest {
    @Test
    public void demoTest() {
        //  用户实际调用
        /**
         UserServiceImpl userService = new UserServiceImpl();
         userService.setUserDao(new UserDaoOracleImpl());
         userService.getUser();
         */

        //  获取ApplicationContext：拿到Spring容器
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        //  需要什么酒get什么
        UserServiceImpl userServiceImpl = (UserServiceImpl) context.getBean("userServiceImpl");
        userServiceImpl.getUser();
    }
}
