package com.test03;

public class testMain03 {
    public static void main(String[] args) {
        /*
         *  同时开启两个线程，共同获取1 ~ 100之间的所有数字
         *  要求输出所有的技术
         */

        MyRunnable runnable = new MyRunnable();
        Thread thread01 = new Thread(runnable);
        Thread thread02 = new Thread(runnable);
        thread01.setName("线程A");
        thread02.setName("线程B");
        thread01.start();
        thread02.start();
    }
}
