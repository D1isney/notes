package com.pojo;

public class Hello {
    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void show() {
        System.out.println("Hello：" + Name);
    }
}
