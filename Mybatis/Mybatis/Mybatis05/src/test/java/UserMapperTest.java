import com.dao.UserMapper;
import com.pojo.User;
import com.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserMapperTest {
    @Test
    public void getUser() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //  底层主要应用反射
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        List<User> user = mapper.getUser();
//        for (User user1 : user) {
//            System.out.println(user1);
//        }

        //  通过id查找
//        User userById = mapper.getUserById05(1);
//        System.out.println(userById);

        //  添加
//        int i = mapper.addUser(new User(7, "", ""));

        //  修改
//        mapper.updateUser(new User(7,"dandan","31"));

        //  删除
        mapper.deleteUser(7);
        sqlSession.close();

    }
}
