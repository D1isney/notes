package com.wms.thread;

import com.wms.pojo.LoginMember;
import com.wms.pojo.Member;

public class MemberThreadLocal {
    public MemberThreadLocal() {
    }

    private static final ThreadLocal<LoginMember> MEMBER_INFO_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 清楚用户信息
     */
    public static void clear(){
        MEMBER_INFO_THREAD_LOCAL.remove();
    }

    /**
     * 存储房钱用户
     * @param member 当前用户
     */
    public static void set(LoginMember member){
        MEMBER_INFO_THREAD_LOCAL.set(member);
    }

    /**
     * 获取当前用户
     * @return 当前用户
     */
    public static LoginMember get(){
        return MEMBER_INFO_THREAD_LOCAL.get();
    }


}
