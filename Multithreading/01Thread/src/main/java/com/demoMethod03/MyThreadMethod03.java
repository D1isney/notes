package com.demoMethod03;

public class MyThreadMethod03 {
    public static void main(String[] args) {
        //  final void setDaemon(boolean on)    设置为守护线程
        //  当其他的飞守护线程执行完毕之后，守护线程会陆续结束，优先让别人执行
        MyThread1 t1 = new MyThread1();
        MyThread2 t2 = new MyThread2();

        t1.setName("1111");
        t2.setName("2222");

        t2.setDaemon(true);

        t1.start();
        t2.start();
    }

}
