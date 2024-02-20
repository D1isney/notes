package com.dao;

import com.pojo.User;
import org.apache.ibatis.annotations.Param;


public interface UserMapper {

    User queryUsersById(@Param("id") int id);

    int updateUser(User user);
}
