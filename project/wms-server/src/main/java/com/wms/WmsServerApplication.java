package com.wms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class WmsServerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(WmsServerApplication.class, args);
//        String[] beanDefinitionNames = run.getBeanDefinitionNames();
//        List<String> list = Arrays.stream(beanDefinitionNames).toList();
//        list.forEach(System.out::println);
        System.out.println("==========>>>>>>>>>>>>WMS 启动成功");
    }

}
