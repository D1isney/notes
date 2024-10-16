package com.demoMethod01;

public class Method01 {
    public static void main(String[] args) throws InterruptedException {
        MyThreadMethod01 t1 = new MyThreadMethod01();
        MyThreadMethod01 t2 = new MyThreadMethod01("线程2");
        t1.setName("线程1");
        t1.start();
        t2.start();
    }
}
