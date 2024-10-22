package com.waitAndNotify02;

import java.util.concurrent.ArrayBlockingQueue;

public class Cook02 extends Thread {

    ArrayBlockingQueue<String> queue;

    public Cook02(ArrayBlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            //  不断的把面条放到阻塞队列中
            try {
                //  底层是有加锁的
                queue.put("面条");
                System.out.println(" 厨师放了一碗面条");
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
