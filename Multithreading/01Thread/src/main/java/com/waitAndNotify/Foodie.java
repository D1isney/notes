package com.waitAndNotify;

public class Foodie extends Thread {
    @Override
    public void run() {
        /*
            1、循环
            2、同步代码块
            3、判断共享数据是否到了末尾（先写到了末尾的情况）
         */
        while (true) {
            synchronized (Desk.lock) {
                if (Desk.count == 0) {
                    break;
                } else {
                    //  先去判断桌子上是否有面条
                    if (Desk.foodFlag == 0) {
                        //  没有就等待
                        try {
                            Desk.lock.wait();// 让当前线程跟锁绑定，唤醒的时候通过锁来唤醒，就会唤醒跟锁绑定的所有线程
//                          例如：  Desk.lock.notify();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        //  把吃的总数减1
                        Desk.count--;
                        //  有面条的情况
                        System.out.println("Foodie：正在吃，还能再吃：" + Desk.count + "碗！");
                        //  吃完唤醒厨师继续做
                        //  唤醒绑定这把锁的所有线程
                        Desk.lock.notifyAll();
                        //  修改桌子的状态值
                        Desk.foodFlag = 0;
                    }
                }
            }
        }

    }
}
