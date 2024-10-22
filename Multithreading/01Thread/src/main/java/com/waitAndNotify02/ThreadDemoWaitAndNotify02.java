package com.waitAndNotify02;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ThreadDemoWaitAndNotify02 {
    public static void main(String[] args) {
        /*
            需求：利用阻塞队列完成生产者和消费者（等待唤醒机制代码）
            生产者和消费者必须使用同一个阻塞队列
         */

        //1、创建阻塞队列
        ArrayBlockingQueue queue = new ArrayBlockingQueue(1);

        //2、创建线程
        Cook02 cook = new Cook02(queue);
        Foodie02 foodie = new Foodie02(queue);
        cook.setName("厨师");
        foodie.setName("食客");
        cook.start();
        foodie.start();

    }
}
