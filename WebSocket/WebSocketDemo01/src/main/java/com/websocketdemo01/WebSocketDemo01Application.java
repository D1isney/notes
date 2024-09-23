package com.websocketdemo01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//  开启定时任务
@EnableScheduling
public class WebSocketDemo01Application {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketDemo01Application.class, args);
    }

}
