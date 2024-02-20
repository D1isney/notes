package com.dao;


import com.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {

    @Select("select * from user")
    List<User> getUser();

    //  方法存在多个参数，所有参数前必须加上@Param
    @Select("select * from user where id = #{id}")
    User getUserById05(@Param("id") int id);

    @Insert("insert into user(id,name,pwd) value (#{id},#{name},#{password})")
    int addUser(User user);

    @Update("update user set name=#{name},pwd=#{password} where id = #{id}")
    int updateUser(User user);

    @Delete("delete from user where id=#{id}")
    int deleteUser(@Param("id") int id);

}
