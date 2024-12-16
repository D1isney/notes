package com.wms.constant;

public interface MemberConstant {
    Integer STATUS_TRUE = 1;//  账号是否可用
    Integer STATUS_FALSE = 0;// 账号不可用
    Integer STATUS_PAST = 2;

    Integer CONTINUE_LOGIN = 400;  //   告诉前端，已经有用户登录过了，返回个400给他，然后如果继续登录，则清楚该请求的请求头
}
