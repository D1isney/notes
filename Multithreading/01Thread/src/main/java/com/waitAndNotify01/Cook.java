package com.waitAndNotify01;

public class Cook extends Thread {

    @Override
    public void run() {
        while (true) {
            synchronized (Desk.lock){
                if (Desk.count == 0){
                    break;
                }else{
                    if (Desk.foodFlag == 1){
                        try {
                            Desk.lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }else{
                        //  没有就做
                        System.out.println("Cook做了一碗面条");
                        Desk.foodFlag = 1;
                        Desk.lock.notifyAll();
                    }
                }
            }
        }
    }
}
