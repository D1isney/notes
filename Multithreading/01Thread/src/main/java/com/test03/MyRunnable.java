package com.test03;

public class MyRunnable implements Runnable {

    private static int count = 1;

    @Override
    public void run() {
        while (count < 100) {
            if (count % 2 == 0) {
                continue;
            } else {
                System.out.println(Thread.currentThread().getName() + "：" + count);
            }
            count++;
        }
    }

}
