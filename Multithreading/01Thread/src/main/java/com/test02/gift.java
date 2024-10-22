package com.test02;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class gift {
    //  一百份礼物
    public static int count = 100;

    //  礼物锁
    public static Object lock = new Object();
}
