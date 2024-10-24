package com.test05;

import java.util.ArrayList;
import java.util.Collections;

public class MyThread extends Thread {
    ArrayList<Integer> list;

    public MyThread(ArrayList<Integer> list) {
        this.list = list;
    }

    @Override
    public void run() {
        ArrayList<Integer> boxList = new ArrayList<>();
        while (true) {
            synchronized (MyThread.class) {
                if (list.isEmpty()) {
                    System.out.println(getName() + boxList);
                    //  抽完了
                    break;
                } else {
                    //  继续抽奖
                    //  打乱集合
                    Collections.shuffle(list);
                    Integer remove = list.remove(0);
                    boxList.add(remove);
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
