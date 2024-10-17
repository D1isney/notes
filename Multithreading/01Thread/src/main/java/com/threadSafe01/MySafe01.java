package com.threadSafe01;

public class MySafe01 extends Thread {

    //  标识这个类所有的对象，都共享ticket数据
    static int ticket = 0;

    //  任意锁对象、一定是唯一的
    static Object lock = new Object();

    @Override
    public void run() {
        while (true) {
            //  同步代码块
//            synchronized (lock) {
            //  多个对象不可以使用this，锁只能是唯一的
            //  MySafe01.class 字节码对象是唯一的
            synchronized (Object.class) {
                if (ticket < 100) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    ticket++;
                    System.out.println(getName() + "正在卖" + ticket + "张票");
                } else {
                    break;
                }
            }
        }

    }
}
