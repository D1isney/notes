package com.wms.connect.websocket;

import com.github.benmanes.caffeine.cache.Cache;
import com.wms.exception.EException;
import com.wms.filter.login.Member;
import com.wms.service.MemberService;
import com.wms.thread.MemberThreadLocal;
import com.wms.utils.JwtUtil;
import com.wms.utils.StringUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class WebTokenProvider {
    @Resource
    private Cache<String, Object> cache;
    @Value("${cache.user-key}")
    private String userKey;
    @Resource
    private MemberService memberService;

    public Member authentication(String token) {
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String userid = claims.getSubject();
        Member member = (Member) cache.getIfPresent(userKey + userid);
        //  缓存没有的情况下 线程拿
        if (StringUtil.isEmpty(member)) {
            Long id = Long.parseLong(userid);
            if (!StringUtil.isEmpty(MemberThreadLocal.getMemberInfoMap(id))) {
                member = MemberThreadLocal.getMemberInfoMap(id).getMember();
            }
        }
        //  数据库拿
        if (StringUtil.isEmpty(member)) {
            Long id = Long.parseLong(userid);
            member = memberService.queryById(id);
        }
        if (StringUtil.isEmpty(member)) {
            throw new EException("WebSocket中，无效Token！！！");
        }
        return member;
    }
}
