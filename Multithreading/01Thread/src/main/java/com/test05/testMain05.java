package com.test05;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class testMain05 {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        Collections.addAll(list, 10, 5, 20, 50, 100, 200, 500, 800, 2, 80, 300, 700);

        MyThread t1 = new MyThread(list);
        MyThread t2 = new MyThread(list);
        MyThread t3 = new MyThread(list);
        t1.setName("t1");
        t2.setName("t2");
        t3.setName("t3");
        t1.start();
        t2.start();
        t3.start();


    }
}
