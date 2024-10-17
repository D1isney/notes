package com.threadSafe02;

import com.threadSafe01.MySafe01;

public class Safe02 {
    public static void main(String[] args) {
        MySafe02 mySafe = new MySafe02();
        Thread t1 = new Thread(mySafe);
        Thread t2 = new Thread(mySafe);
        Thread t3 = new Thread(mySafe);
        t1.setName("售票员1");
        t2.setName("售票员2");
        t3.setName("售票员3");
        t1.start();
        t2.start();
        t3.start();

    }
}

