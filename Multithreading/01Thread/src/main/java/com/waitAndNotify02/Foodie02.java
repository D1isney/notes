package com.waitAndNotify02;

import java.util.concurrent.ArrayBlockingQueue;

public class Foodie02 extends Thread {

    ArrayBlockingQueue<String> queue;

    public Foodie02(ArrayBlockingQueue<String> queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        while(true){
            //  不断从阻塞队列中获取面条
            try {
                //  底层是有加锁的
                String take = queue.take();
                System.out.println(Thread.currentThread().getName()+"："+take);
            }catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
