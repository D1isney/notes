package com.demo03;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Thread03 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*
            多线程的第三种实现方式
            特点：可以获取到多线程运行的结果
            1、创建一个类MyCallable 实现Callable接口
            2、重写call（是有返回值的，表示多线程运行的结果）
            3、创建MyCallable的对象（表示多线程要执行的任务）
            4、创建Future的对象（作用管理多线程的运行结果）
            5、创建Thread类的对象（表示线程）
         */

        //  创建MyCallable的对象（表示多线程要执行的任务）
        MyCallable myCallable = new MyCallable();
        //  Future是一个接口，所以要创建他的实现类对象
        //  创建FutureTask的对象（作用管理多线程运行的结果）
        FutureTask<Object> ft = new FutureTask<>(myCallable);
        //  创建线程对象
        new Thread(ft).start();
        Object o = ft.get();
        System.out.println(o);
    }
}
