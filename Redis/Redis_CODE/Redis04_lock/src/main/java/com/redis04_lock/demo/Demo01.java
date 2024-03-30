package com.redis04_lock.demo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demo01 {
    final Object obj = new Object();

    public void entry01() {
        new Thread(() -> {
            synchronized (obj) {
                System.out.println(Thread.currentThread().getName() + "\t" + "外层调用");
                synchronized (obj) {
                    System.out.println(Thread.currentThread().getName() + "\t" + "中层调用");
                    synchronized (obj) {
                        System.out.println(Thread.currentThread().getName() + "\t" + "内层调用");
                    }
                }
            }
        }, "t1").start();
    }

    public void entry02() {
        m1();
    }

    private synchronized void m1() {
        System.out.println(Thread.currentThread().getName() + "\t" + "外层调用");
        m2();
    }

    private synchronized void m2() {
        System.out.println(Thread.currentThread().getName() + "\t" + "中层调用");
        m3();
    }

    private synchronized void m3() {
        System.out.println(Thread.currentThread().getName() + "\t" + "内层调用");
    }

    Lock lock = new ReentrantLock();
    public void entry03() {
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t" + "外层调用");
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "\t" + "中层调用");
                    lock.lock();
                    try {
                        System.out.println(Thread.currentThread().getName() + "\t" + "内层调用");
                    } finally {
//                        t4就拿不到这个锁
                        lock.unlock();
                    }
                } finally {
                    lock.unlock();
                }
            } finally {
                lock.unlock();
            }
        }, "t3").start();

        //  暂停两秒
        try {
            TimeUnit.MILLISECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t" + "外层调用+t4");
            } finally {
                lock.unlock();
            }
        }, "t4").start();
    }

    public static void main(String[] args) {
        Demo01 demo01 = new Demo01();
//        demo01.entry01();
        System.out.println();
        demo01.entry03();
    }
}