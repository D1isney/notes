package com.redis02_demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    //  配置了Swagger的Docket的Bean实例
    @Bean
    public Docket api1(Environment environment) {

        //  设置要显示的Swagger环境
        Profiles profiles = Profiles.of("dev","test");
        //  获取项目的环境：
        //  通过environment.acceptsProfiles判断是否处在自己设定的环境当中
        boolean b = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //  requestHandlerSelectors 配置要扫描接口的方式
                //  basePackage指定要扫描的包
                //  any()：扫描全部
                //  none()：不扫描
                //  withClassAnnotation()：扫描类上的注解
                //  withMethodAnnotation()：烧苗方法上的注解
                //  paths()：过滤什么路径
                //  enable是否启动Swagger，如果为false，则Swagger不能再在浏览器启动，默认值为true
                .apis(RequestHandlerSelectors.basePackage("com.redis02_demo.controller"))
                .build()
                .enable(b)
                //  分组
                .groupName("Disney");
    }

    @Bean
    public Docket api2(Environment environment) {

        //  设置要显示的Swagger环境
        Profiles profiles = Profiles.of("dev","test");
        //  获取项目的环境：
        //  通过environment.acceptsProfiles判断是否处在自己设定的环境当中
        boolean b = environment.acceptsProfiles(profiles);

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .build()
                .enable(b)
                //  分组
                .groupName("蛋蛋");
    }

    //  配置Swagger信息=apiInfo
    private ApiInfo apiInfo(){
        //  作者信息
        Contact contact = new Contact("蛋蛋", "https://localhost:8080", "2497050447@qq.com");
        return  new ApiInfo(
                "DisneyD33接口日志",
                "蛋蛋",
                "v1.0",
                "https:localhost:8080",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
}
