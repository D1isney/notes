package com.wms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
//  权限认证注解
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WmsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WmsServerApplication.class, args);
        System.out.println("=========================================================");
        System.out.println("=========================================================");
        System.out.println("==========>>>>>>>>>>>>WMS 启动成功");
        System.out.println("=========================启动时间=========================");
        System.out.println("=================="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"===================");
        System.out.println("=========================================================");
        System.out.println("=========================================================");
    }

}
