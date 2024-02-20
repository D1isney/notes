package com.config;

import com.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

//  本质跟Component一样
//  Configuration代表这个一个配置类，和beans.xml一样
@Configuration
@ComponentScan("com.pojo")
//  通过Import来引入别的Config
@Import(DisneyConfig.class)
public class UserConfig {

    //  注册一个Bean，就相当于之前写的一个Bean标签
    //  方法名就相当于Bean标签的id属性
    //  方法返回值，就相当于Bean标签的class属性
    @Bean
    public User myUser(){
        //  就是返回要注入到的Bean的对象
        return new User();
    }

}
