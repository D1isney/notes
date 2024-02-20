package com.service;

import com.dao.UserDao;
import com.dao.UserDaoImpl;
import com.dao.UserDaoMySqlImpl;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    //     利用set进行动态实现值的注入
    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void getUser() {
        userDao.getUser();
    }
}
