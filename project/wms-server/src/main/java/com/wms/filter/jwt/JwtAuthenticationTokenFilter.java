package com.wms.filter.jwt;

import com.wms.constant.MemberConstant;
import com.wms.dao.MemberDao;
import com.wms.filter.login.LoginMember;
import com.wms.filter.login.Member;
import com.wms.thread.MemberThreadLocal;
import com.wms.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private MemberDao memberDao;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取认证信息
        String authorization = request.getHeader("Authorization");
        if (!StringUtils.hasText(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(authorization);
            userid = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("非法Authorization");
        }
        Member member = memberDao.selectById(Long.parseLong(userid));
        if (Objects.isNull(member)) {
            throw new RuntimeException("非法用户");
        }
        member.setStatus(MemberConstant.STATUS_TRUE);
        Long id = member.getId();
        List<String> permissionsByMember = memberDao.getPermissionsByMember(id);
        memberDao.updateById(member);
        LoginMember loginMember = new LoginMember();
        //TODO: 需要获取权限
        loginMember.setMember(member);
        loginMember.setPermissions(permissionsByMember);
        MemberThreadLocal.set(loginMember);
//        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginMember, null, loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}

