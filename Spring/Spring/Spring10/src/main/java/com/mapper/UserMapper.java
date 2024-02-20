package com.mapper;

import com.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface UserMapper {
    List<User> selectUser();
}
