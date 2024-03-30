package com.redis04_lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Redis04LockApplication {

    public static void main(String[] args) {
        SpringApplication.run(Redis04LockApplication.class, args);
    }

}
