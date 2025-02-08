package com.wms.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wms.task.Pool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class CommonConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名，这里使用通配符
                .allowedOriginPatterns("*")
                // 是否允许cookie
                .allowCredentials(true)
                // 设置允许的请求方式
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedMethods("*")
                // 设置允许的header属性
                .allowedHeaders(CorsConfiguration.ALL)
                // 跨允许时间
                .maxAge(3600);
    }



    @Value("${thread.pool.corePoolSize}")
    private int corePoolSize;

    @Value("${thread.pool.maximumPoolSize}")
    private int maximumPoolSize;

    @Value("${thread.pool.keepAliveTime}")
    private long keepAliveTime;

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        return new Pool(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    }


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


    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

}
