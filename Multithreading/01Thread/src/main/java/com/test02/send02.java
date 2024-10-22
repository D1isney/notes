package com.test02;

import java.util.concurrent.ArrayBlockingQueue;

public class send02 extends Thread {
    ArrayBlockingQueue<String> queue;

    public send02(ArrayBlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (gift.count >= 10) {
            synchronized (gift.lock) {
                try {
                    if (queue.isEmpty()) {
                        gift.lock.notifyAll();
//                        Thread.yield();
                    } else {
                        String take = queue.take();
                        System.out.println(Thread.currentThread().getName() + "：" + take);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
