package com.test01;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

public class Windows02 extends Thread{
    @Override
    public void run() {
        Date newData = new Date();
        long time = newData.getTime();
        while (time - Ticket.date.getTime() <= 30000) {
            newData = new Date();
            time = newData.getTime();
            //  拿到电影票的锁
            synchronized (Ticket.lock){
                try {
                    Thread.sleep(500);
                    System.out.println(Thread.currentThread().getName()+"卖出一张");
                    Ticket.ticket--;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    Ticket.lock.notifyAll();
                }
            }

        }
    }
}
