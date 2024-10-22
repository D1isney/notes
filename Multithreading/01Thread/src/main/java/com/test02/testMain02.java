package com.test02;

import java.util.concurrent.ArrayBlockingQueue;

public class testMain02 {
    public static void main(String[] args) {
        /*
            有100份礼品，两人同时发送，当剩下的礼品小于10份的时候则不再送出。
            利用多线程模拟该过程并将线程的名字和礼物的数量打印出来
         */

        //1、两个人同时发送，则做三个线程，做一个线程负责塞礼品，两个线程负责发礼物
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(2);
        //2、创建线程
        place place = new place(queue);
        send01 send01 = new send01(queue);
        send02 send02 = new send02(queue);
        send01.setName("Send01");
        send02.setName("Send02");
        place.start();
        send01.start();
        send02.start();
        try {
            place.join();
            send01.join();
            send02.join();
        }catch (InterruptedException e){
            System.out.println(e.getMessage());
        }

        System.out.println("礼物：" + gift.count);
    }
}
