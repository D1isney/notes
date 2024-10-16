package com.demo01;

public class Thread01 {
    /**
     * 多线程的第一种启动方式
     * 1、自己定义一个类继承Thread
     * 2、重写run方法
     * 3、创建子类的对象，并启动线程
     */

    public static void main(String[] args) {
        MyThread t1 = new MyThread();
        MyThread t2 = new MyThread();

        t1.setName("Thread1");
        t2.setName("Thread2");
        t1.start();
        t2.start();
    }
}
