package com.demoMethod02;

public class MyThreadMethod02 {

    public static void main(String[] args) {
    /*
        setPriority(int newPriority)    设置线程的优先级
        final int getPriority   获取线程的优先级
     */
        MyRunnable runnable = new MyRunnable();
        Thread t1 = new Thread(runnable,"飞机");
        Thread t2 = new Thread(runnable,"坦克");
        System.out.println(t1.getPriority());
        System.out.println(t2.getPriority());
        System.out.println(Thread.currentThread().getPriority());

        t1.setPriority(1);
        t2.setPriority(10);
        t1.start();
        t2.start();
    }
}
