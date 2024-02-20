package com.demo04;

public class UserServiceImpl implements UserService {
    @Override
    public void add() {
        System.out.println("增加一个用户");
    }

    @Override
    public void delete() {
        System.out.println("删除一个用户");
    }

    @Override
    public void update() {
        System.out.println("修改一个用户");
    }

    @Override
    public void query() {
        System.out.println("查询一个用户");
    }

    /*
    * 1.改动原有的代码，可能会把业务逻辑改乱
    * */
}
