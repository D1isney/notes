package com.rabbitmq_springboot.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket webApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("WebApi")
                .apiInfo(webApiInfo())
                .select().build();
    }

    private ApiInfo webApiInfo(){
        return new ApiInfoBuilder().title("RabbitMQ接口文档")
                .description("此文档描述了RabbitMQ微服务接口定义")
                .version("1.0")
                .contact(
                        new Contact
                                ("Disney",
                                        "https://blog.csdn.net/weixin_55801899?spm=1000.2115.3001.5343",
                                        "zhziqian2022@163.com"))
                .build();
    }
}
