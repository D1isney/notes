package com.deadLock01;

public class DeadDemo {
    public static void main(String[] args) {
        DeadThread t1 = new DeadThread();
        DeadThread t2 = new DeadThread();
        t1.setName("线程A");
        t2.setName("线程B");
        t1.start();
        t2.start();
    }
}
