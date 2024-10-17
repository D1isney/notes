package com.demoMethod05;

public class MyThreadMethod05 {
    public static void main(String[] args) {
        /*
            public void join()     插入线程、插队线程
         */

        MyThread1 t1 = new MyThread1();
        t1.setName("1");
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        //  执行在main线程当中的
        for (int i = 0; i < 10; i++) {
            System.out.println("main" + i);
        }
    }
}
