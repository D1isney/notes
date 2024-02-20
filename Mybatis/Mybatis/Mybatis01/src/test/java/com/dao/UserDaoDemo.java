package com.dao;

import com.pojo.User;
import com.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDaoDemo {

    //    模糊查询
    @Test
    public void gerUserLike() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
/*
        List<User> users = mapper.gerUserLike("蛋蛋");
        for (User u : users
        ) {
            System.out.println(u);
        }*/
        sqlSession.close();

    }

    @Test
    public void test() {
        //  第一步：获得sqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        try {
            //  执行SQLq
            //  方式一
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = mapper.getUserList();
            for (User user : userList
            ) {
                System.out.println(user);
            }
            System.out.println();

            //  方式二
            List<User> objectList = sqlSession.selectList("com.dao.UserMapper.getUserList");
            for (User user : objectList) {
                System.out.println(user);
            }

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        } finally {
            //  关闭sqlSession
            sqlSession.close();
        }
    }

    @Test
    public void getUserById() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User userById = mapper.getUserById(1);
        System.out.println(userById);
        sqlSession.close();
    }

    //    通过map去查询
    @Test
    public void getUserById2() {
/*        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Map<String, Object> map = new HashMap<>();
        map.put("userName", "张三");
        map.put("userId", 1);
        User userById2 = mapper.getUserById2(map);
        System.out.println(userById2);
        sqlSession.close();*/
    }


    //    增删改需要提交事务
    @Test
    public void addUser() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int res = mapper.addUser(new User(4, "蛋蛋", "121700"));
        if (res > 0) {
            System.out.println("插入成功！");
        }
        //  提交事务
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void addUser2() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        Map<String, Object> map = new HashMap<>();
//        map.put("userId", 5);
//        map.put("userName", "123");
//        map.put("passWord", "123");
//        mapper.addUser2(map);
        sqlSession.commit();
        sqlSession.close();
    }


    @Test
    public void updateUser() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.updateUser(new User(4, "Disney蛋蛋", "001217"));
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void deleteUser() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.deleteUser(4);
        sqlSession.commit();
        sqlSession.close();
        ;
    }
}
