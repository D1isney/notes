package com.mapper;

import com.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    List<User> selectUser();

    //  添加用户
    int addUser(User user);

    int deleteUser(@Param("id") int id);
}
