package com.wms.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 缓存配置类
 */
@Configuration
public class CacheConfig {

    @Value("${cache.expiration-time}")
    private Integer expirationTime;

    @Value("${cache.space-size}")
    private Integer spaceSize;

    @Value("${cache.size}")
    private Integer cacheSize;

    @Bean
    public Cache<String, Object> caffeineCache() {
        return Caffeine.newBuilder()
                // 最后一次写操作后经过指定时间过期
                .expireAfterWrite(expirationTime, TimeUnit.SECONDS)
                // 最后一次读或写操作后经过指定时间过期 与expireAfterWrite同时出现以expireAfterWrite为准
//                .expireAfterAccess(expirationTime, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(spaceSize)
                // 缓存的最大条数
                .maximumSize(cacheSize)
                //  弱引用，当key或者values都不怎么用的时候，就会把他们给回收
//                .weakKeys().weakValues()
                //  打开value的软引用
                .softValues()
                .build();
    }
}
