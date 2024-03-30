package com.redis04_lock.entity;

public class Student extends Person{
    private String sno;
    public void a(){}

    @Override
    public void run(){
        //  重写
        System.out.println("Student在跑步");
    }

    public void run(String name){
        //  重载
        System.out.println(name+"在跑步");
    }
    public void run(String name,int age){
        //  重载
        System.out.println(name+"在跑步");
    }
    public void run(int age){
        //  重载
        System.out.println(age+"在跑步");
    }
    public void run(int age,String name){
        //  重载
        System.out.println(name+"在跑步");
    }
}
