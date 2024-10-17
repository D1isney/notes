package com.threadSafe02;

public class MySafe02 implements Runnable {
    int ticket = 0;

    @Override
    public void run() {
        while (true) {
            if (extracted()) break;
        }
    }

    private synchronized boolean extracted() {
        if (ticket <= 99) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            ticket++;
            System.out.println(Thread.currentThread().getName() + "正在卖" + ticket + "张票");
        } else {
            return true;
        }
        return false;
    }
}
