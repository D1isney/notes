package com.pojo;

public class UserD {
    private String name;

    public UserD() {
        System.out.println("UserD无参构造函数："+ null);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void show() {
        System.out.println("name：" + name);
    }
}
