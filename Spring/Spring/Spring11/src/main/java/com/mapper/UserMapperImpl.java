package com.mapper;

import com.pojo.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.List;

public class UserMapperImpl extends SqlSessionDaoSupport implements UserMapper{


    @Override
    public List<User> selectUser() {

        User user = new User(7, "张三", "123");
        UserMapper mapper1 = getSqlSession().getMapper(UserMapper.class);
        mapper1.addUser(user);
        mapper1.deleteUser(7);

        UserMapper mapper = getSqlSession().getMapper(UserMapper.class);
        List<User> users = mapper.selectUser();
        return users;
    }

    @Override
    public int addUser(User user) {
        return getSqlSession().getMapper(UserMapper.class).addUser(user);
    }

    @Override
    public int deleteUser(int id) {
        return getSqlSession().getMapper(UserMapper.class).deleteUser(id);
    }
}
