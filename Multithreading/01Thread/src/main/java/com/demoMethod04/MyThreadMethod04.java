package com.demoMethod04;

public class MyThreadMethod04 {
    public static void main(String[] args) {
        /*
            public void yield()     出让线程、礼让线程
         */

        MyThread1 t1 = new MyThread1();
        MyThread1 t2 = new MyThread1();
        t1.setName("1");
        t2.setName("2");

        t1.start();


        t2.start();
    }
}
