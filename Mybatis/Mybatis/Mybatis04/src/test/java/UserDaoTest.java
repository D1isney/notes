import com.dao.UserMapper;
import com.pojo.User;
import com.utils.MybatisUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;


public class UserDaoTest {
    //    提升作用域
    static Logger logger = Logger.getLogger(UserDaoTest.class);

    @Test
    public void getUserList() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        logger.info("getUserList，进入成功！");
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User userById = mapper.getUserById(1);
        System.out.println(userById);
        sqlSession.close();
    }

    @Test
    public void testLog4j() {
        System.out.println();
        logger.info("info:进入了testLog4j");
        logger.debug("debug:进入了testLog4j");
        logger.error("error:进入了testLog4j");
    }

    @Test
    public void getUserByLimit(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        HashMap<String, Integer> map = new HashMap<>();
        map.put("startIndex",0);
        map.put("pageSize",2);
        List<User> userByLimit = mapper.getUserByLimit(map);

        for (User user : userByLimit) {
            System.out.println(user);
        }

        sqlSession.close();
    }


    @Test
    public void getUserByRowBounds(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();

//        RowBounds 对象
        RowBounds bounds = new RowBounds(1,2);

//        通过Java代码层面实现分页
        List<Object> objects = sqlSession.selectList("com.dao.UserMapper.getUserByRowBounds",null,bounds);
        for (Object object : objects) {
            System.out.println(object);
        }

        sqlSession.close();
    }
}
