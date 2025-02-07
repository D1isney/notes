package com.wms.config;

import com.wms.task.Pool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class PoolConfig {
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

}
