package com.threadSafe03;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MySafe03 extends Thread {
    static int ticket = 0;

    static Lock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
//            synchronized (MySafe03.class) {
            lock.lock();
            try {
                if (ticket == 100) {
                    //  结束的时候要释放锁，如果不清楚释放的时间，可以在finally释放
//                lock.unlock();
                    break;
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    ticket++;
                    System.out.println(getName() + "在卖第" + ticket + "张票");
                }
//            lock.unlock();
            } finally {
                lock.unlock();
            }
        }

//        }
    }
}
