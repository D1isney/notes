package com.demo03;

import java.util.concurrent.Callable;

public class MyCallable implements Callable<Object> {
    //  Callable<T>泛型就是返回数据的类型
    @Override
    public Object call() throws Exception {
        //  求1到100之间的和
        int sum = 0;
        for (int i = 1 ;i<101;i++){
            sum += i;
        }
        return sum;
    }
}
