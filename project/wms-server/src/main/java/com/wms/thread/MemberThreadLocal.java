package com.wms.thread;

import com.wms.filter.login.LoginMember;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemberThreadLocal {
    public MemberThreadLocal() {
    }

    //  当前用户
    private static final ThreadLocal<LoginMember> MEMBER_INFO_THREAD_LOCAL = new ThreadLocal<>();
    //  总用户
    private static final Map<Long, LoginMember> ALL_MEMBER_CURRENT_ID = new ConcurrentHashMap<>();
    //  所有用户的token
    private static final Map<Long, String> ALL_MEMBER_CURRENT_TOKEN_FOR_ID = new ConcurrentHashMap<>();


    /**
     * 清楚用户信息
     */
    public static void clear() {
        MEMBER_INFO_THREAD_LOCAL.remove();
    }

    /**
     * 存储当前用户
     *
     * @param member 当前用户
     */
    public static void set(LoginMember member) {
        MEMBER_INFO_THREAD_LOCAL.set(member);
    }

    /**
     * 获取当前用户
     *
     * @return 当前用户
     */
    public static LoginMember get() {
        return MEMBER_INFO_THREAD_LOCAL.get();
    }


    //  添加
    public static void setMainThreadLoginMemberById(Long id, LoginMember member) {
        ALL_MEMBER_CURRENT_ID.put(id, member);
    }

    //  删除
    public static void clearMainThreadLoginMemberById(Long id) {
        ALL_MEMBER_CURRENT_ID.remove(id);
    }

    //  获取
    public static LoginMember getMemberInfoMap(Long id) {
        return ALL_MEMBER_CURRENT_ID.get(id);
    }

    public static void setMainThreadLoginMemberTokenForId(Long id, String token) {
        ALL_MEMBER_CURRENT_TOKEN_FOR_ID.put(id, token);
    }
    public static void clearMainThreadLoginMemberTokenForId(Long id) {
        ALL_MEMBER_CURRENT_TOKEN_FOR_ID.remove(id);
    }
    public static String getMainThreadLoginMemberTokenForId(Long id) {
        return ALL_MEMBER_CURRENT_TOKEN_FOR_ID.get(id);
    }
}
