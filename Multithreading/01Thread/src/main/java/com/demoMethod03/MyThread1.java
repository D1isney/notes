package com.demoMethod03;

public class MyThread1 extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(getName() + "@" + i);
        }
    }
}
