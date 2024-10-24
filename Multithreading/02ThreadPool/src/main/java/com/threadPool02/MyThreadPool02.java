package com.threadPool02;

import java.util.concurrent.*;

public class MyThreadPool02 {
    public static void main(String[] args) {
        ThreadPoolExecutor pool =
                new ThreadPoolExecutor(
                        3, // 核心线程，能小于0
                        3, //   最大线程数，不能小于0，最大数 >= 核心线程数
                        6000, //    时间数量
                        TimeUnit.SECONDS, //    时间单位
                        new LinkedBlockingQueue<Runnable>(),
                        Executors.defaultThreadFactory(),   // 创建线程的工厂
                        new ThreadPoolExecutor.CallerRunsPolicy() //    任务的拒绝策略

                ); //    阻塞队列

        pool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("123");
            }
        });
    }
}
