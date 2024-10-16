package com.demoMethod01;

public class MyThreadMethod01 extends Thread {
    public MyThreadMethod01(String name) {
        super(name);
    }

    public MyThreadMethod01() {
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(getName() + "@" + i);
        }
    }
}
