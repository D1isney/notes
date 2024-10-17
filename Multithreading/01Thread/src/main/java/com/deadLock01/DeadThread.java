package com.deadLock01;

public class DeadThread extends Thread {
    static Object lock1 = new Object();
    static Object lock2 = new Object();

    @Override
    public void run() {
        while (true) {
            if ("线程A".equals(getName())) {
                synchronized (lock1) {
                    System.out.println("线程A拿到了A锁，准备拿B锁");
                    synchronized (lock2) {
                        System.out.println("线程A拿到了B锁，顺利一轮拿完");
                    }
                }
            }else if ("线程B".equals(getName())){
                synchronized (lock2) {
                    System.out.println("线程B拿到了B锁，准备拿A锁");
                    synchronized (lock1) {
                        System.out.println("线程B拿到了A锁，顺利一轮拿完'");
                    }
                }
            }
        }
    }
}
