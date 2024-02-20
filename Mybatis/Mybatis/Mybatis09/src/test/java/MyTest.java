import com.dao.UserMapper;
import com.pojo.User;
import com.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class MyTest {

    @Test
    public void test(){
        SqlSession sqlSession1 = MybatisUtils.getSqlSession();
        SqlSession sqlSession2 = MybatisUtils.getSqlSession();

        UserMapper mapper1 = sqlSession1.getMapper(UserMapper.class);
        UserMapper mapper2 = sqlSession1.getMapper(UserMapper.class);

        User user1 = mapper1.queryUsersById(1);
        System.out.println(user1);

        User user2 = mapper2.queryUsersById(1);
        System.out.println(user2);


        sqlSession1.close();
        sqlSession2.close();
    }
}
