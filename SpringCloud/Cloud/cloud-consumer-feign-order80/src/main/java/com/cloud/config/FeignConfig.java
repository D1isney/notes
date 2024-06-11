package com.cloud.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Retryer myRetryer() {
        //  Feign默认配置是没有重试策略的
//        return Retryer.NEVER_RETRY;

        //  最大请求数为4（1+3），初始间隔为100ms，重试时间最大间隔时间为1s
        return new Retryer.Default(100, 1, 4);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
