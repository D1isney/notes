package com.test01;

import java.util.concurrent.ArrayBlockingQueue;

public class testMain01 {
    public static void main(String[] args) {
        /*
         *  一共有1000张电影票，可以在两个窗口领取，假设每次领取的时间为3000毫秒
         *  要求：请用多线程模拟卖票过程并打印剩余电影票的数量
         */

        //  1、最多同时两个线程卖票
        Windows01 win01 = new Windows01();
        Windows02 win02 = new Windows02();
        win01.setName("Windows01");
        win02.setName("Windows02");
        win01.start();
        win02.start();
        try {
            win01.join();
            win02.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //  让上面的执行完
//        Thread.yield();
        System.out.println("剩余："+Ticket.ticket);
    }
}
