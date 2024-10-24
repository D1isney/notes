package com.threadPool01;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyThreadPool01 {
    public static void main(String[] args) {
        //  1、获取线程池对象
        ExecutorService executorService = Executors.newCachedThreadPool();
        //  创建有大小限制的线程池
        ExecutorService executorService1 = Executors.newFixedThreadPool(10);

        //  穿件线程
        MyRunnable01 myRunnable01 = new MyRunnable01();
        //  2、提交任务
        executorService.submit(myRunnable01);
        executorService.submit(new MyRunnable01());
        executorService.submit(new MyRunnable01());
        executorService.submit(new MyRunnable01());
        executorService.submit(new MyRunnable01());
        executorService.submit(new MyRunnable01());

        //  3、销毁线程池
        executorService.shutdown();
    }
}
