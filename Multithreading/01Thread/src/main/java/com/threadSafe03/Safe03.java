package com.threadSafe03;

public class Safe03 {
    public static void main(String[] args) {
        MySafe03 t1 = new MySafe03();
        MySafe03 t2 = new MySafe03();
        MySafe03 t3 = new MySafe03();
        t1.setName("售票员1");
        t2.setName("售票员2");
        t3.setName("售票员3");
        t1.start();
        t2.start();
        t3.start();
    }
}
