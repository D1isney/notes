package com.dao;

import com.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface TeacherMapper {
    //  获取所有老师
    List<Teacher> getTeacher();


    //  获取制定老师下的所有学生及老师的信息
    Teacher getTeacher(@Param("tid") int id);



    Teacher getTeacher2(@Param("tid") int id);

}
