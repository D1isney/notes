package com.test04;

public class testMain04 {
    public static void main(String[] args) {
        /*
            微信中的抢红包也用到了线程。
            假设：100，分成3个包，现在有5个人去抢
            其中，红包是共享数据。
            5个人就5条县城
            打印结果如下：
                xxx抢到xx元
                xxx抢到xx元
                xxx抢到xx元
                xxx没抢到
                xxx没抢到
         */

        //  创建线程对象
        testThread01 t1 = new testThread01();
        testThread01 t2 = new testThread01();
        testThread01 t3 = new testThread01();
        testThread01 t4 = new testThread01();
        testThread01 t5 = new testThread01();

        t1.setName("11");
        t2.setName("22");
        t3.setName("33");
        t4.setName("44");
        t5.setName("55");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}
