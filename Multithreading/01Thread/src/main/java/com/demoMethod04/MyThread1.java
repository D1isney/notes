package com.demoMethod04;

public class MyThread1 extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(getName() + "@" + i);

            //  出让当前CPU的执行权
            Thread.yield();
        }
    }
}
