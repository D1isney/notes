package com.test02;

import java.util.concurrent.ArrayBlockingQueue;

//  放置礼物
public class place extends Thread {

    ArrayBlockingQueue<String> queue;

    public place(ArrayBlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        //  礼物小于10的时候不放礼物
        while (gift.count >= 10) {
            synchronized (gift.lock) {
                try {
                    if (queue.size() == 2) {
                        gift.lock.notifyAll();
                    } else {
                        queue.put("礼物");
                        gift.count--;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
