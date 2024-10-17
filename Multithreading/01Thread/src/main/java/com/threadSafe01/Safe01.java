package com.threadSafe01;

public class Safe01 {
    public static void main(String[] args) {
        /*
            需求：
                某电影院目前正在上映国产大片，共有100张票，而它只有3个窗口卖票，请设计一个程序模拟该电影院卖票
         */

        MySafe01 mySafe01 = new MySafe01();
        MySafe01 mySafe02 = new MySafe01();
        MySafe01 mySafe03 = new MySafe01();
        mySafe01.setName("窗口1");
        mySafe02.setName("窗口2");
        mySafe03.setName("窗口3");

        mySafe01.start();
        mySafe02.start();
        mySafe03.start();

    }
}
