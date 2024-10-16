package com.demo02;

public class MyRun implements Runnable {
    @Override
    public void run() {
        //  书写线程要执行的代码
        for (int i = 0; i < 100; i++) {
            //  获取当前线程对象
            Thread thread = Thread.currentThread();
            //  getName是在Thread里面的
            System.out.println(thread.getName() + "：Hello World!");
        }
    }
}
