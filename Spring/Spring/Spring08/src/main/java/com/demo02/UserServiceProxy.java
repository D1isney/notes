package com.demo02;

public class UserServiceProxy implements UserService {
    UserServiceImpl userService;

    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public void add() {
        log("add");
        userService.add();
    }

    @Override
    public void delete() {
        log("delete");
        userService.delete();
    }

    @Override
    public void update() {
        log("update");
        userService.update();
    }

    @Override
    public void query() {
        log("query");
        userService.delete();
    }

    public void log(String str) {
        System.out.println("使用了" + str + "方法");
    }
}
