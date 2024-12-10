package com.wms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
//  权限认证注解
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WmsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WmsServerApplication.class, args);
        System.out.println("==========>>>>>>>>>>>>WMS 启动成功");
    }

}
