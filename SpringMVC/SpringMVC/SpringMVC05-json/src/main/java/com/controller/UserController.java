package com.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pojo.User;
import com.utils.JsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Controller
//  不走视图解析器
@RestController
public class UserController {


    @RequestMapping("/json1")
    //  解决编码错误的问题
    //    @RequestMapping(value = "/json1",produces = "application/json;charset=utf-8")
//    @ResponseBody //不会走视图解析器，会直接返回一个字符串
    public String json1() throws JsonProcessingException {

        //  创建一个对象
        User user = new User("Disney", 22, "男");

        //  jackson , ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(user);

        return str;
    }


    /**
     * 返回集合
     */
    @RequestMapping("/json2")
//    @ResponseBody
    public String json2() throws JsonProcessingException {

        List<User> userList = new ArrayList<>();
        //  创建多个对象
        User user1 = new User("Disney1", 22, "男");
        User user2 = new User("Disney2", 22, "男");
        User user3 = new User("Disney3", 22, "男");
        User user4 = new User("Disney4", 22, "男");

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);

        //  jackson , ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(userList);

        return JsonUtils.getJson(userList);
        /**
         *
         * [
         *  {"name":"Disney1","age":22,"sex":"男"},
         *  {"name":"Disney2","age":22,"sex":"男"},
         *  {"name":"Disney3","age":22,"sex":"男"},
         *  {"name":"Disney4","age":22,"sex":"男"}
         * ]
         *
         */
    }


    @RequestMapping("/json3")
    public String json3() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        //  Timestamp  时间戳  默认格式

        //  第一种
        //  自定义的格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = format.format(date);
        //  2023-11-21 22:17:13


        //  第二种
        //  使用ObjectMapper 来格式化
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //  自行注入格式
        mapper.setDateFormat(format);

        return mapper.writeValueAsString(date);
    }

    @RequestMapping("/json4")
    public String json4() throws JsonProcessingException {

        List<User> userList = new ArrayList<>();
        //  创建多个对象
        User user1 = new User("Disney1", 22, "男");
        User user2 = new User("Disney2", 22, "男");
        User user3 = new User("Disney3", 22, "男");
        User user4 = new User("Disney4", 22, "男");
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);

        String str = JSON.toJSONString(userList);

        return str;
    }

}
