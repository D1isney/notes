package com.wms.filter.jwt;

import com.wms.constant.MemberConstant;
import com.wms.dao.MemberDao;
import com.wms.exception.EException;
import com.wms.filter.login.LoginMember;
import com.wms.filter.login.Member;
import com.wms.thread.MemberThreadLocal;
import com.wms.utils.HttpUtil;
import com.wms.utils.JwtUtil;
import com.wms.utils.R;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
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

import static com.wms.constant.MemberConstant.CONTINUE_LOGIN;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private MemberDao memberDao;

    @Value("${login.filter.tokenIsReal}")
    private Integer tokenIsReal;

    @Value("${login.filter.header}")
    private String header;

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //  鉴定JWT的有效性
        String authorization = request.getHeader(header);
        if (!StringUtils.hasText(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(authorization);
            userid = claims.getSubject();
        } catch (Exception e) {
            throw new EException("非法Authorization");
        }
        Member member = memberDao.selectById(Long.parseLong(userid));
        if (Objects.isNull(member)) {
            throw new EException("非法用户");
        }

        if (tokenIsReal > 0) {
            boolean tokenIsReal = tokenIsReal(userid, response,authorization);
            if (!tokenIsReal) {
                return;
            }
        }
        member.setOnline(MemberConstant.IS_ONLINE);
        Long id = member.getId();
        List<String> permissionsByMember = memberDao.getPermissionsByMember(id);
        memberDao.updateById(member);
        LoginMember loginMember = new LoginMember();
        loginMember.setMember(member);
        if (!permissionsByMember.isEmpty() && permissionsByMember.size() > 1) {
            loginMember.setPermissions(permissionsByMember);
        } else {
            loginMember.setPermissions(new ArrayList<>());
        }
        //  只需要存当前的用户
        MemberThreadLocal.set(loginMember);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginMember, null, loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }


    /**
     * 判断用户的token是否有效
     *
     * @param userid   用户id
     * @param response 响应体
     * @return true：有效 false：无效
     * @throws IOException 异常
     */
    public boolean tokenIsReal(String userid, HttpServletResponse response,String authorization) throws IOException {
        //  校验用户的token是否有效的
        String mainThreadLoginMemberTokenForId = MemberThreadLocal.getMainThreadLoginMemberTokenForId(Long.valueOf(userid));
        if (Objects.isNull(mainThreadLoginMemberTokenForId)) {
            R<Object> r = R.error("用户已失效，请重新登录！");
            r.setCode(CONTINUE_LOGIN);
            HttpUtil.responseJSON((HttpServletResponse) response, r);
            return false;
        }
        if (!Objects.equals(mainThreadLoginMemberTokenForId, authorization)) {
            R<Object> r = R.error("用户已失效，请重新登录！");
            r.setCode(CONTINUE_LOGIN);
            HttpUtil.responseJSON((HttpServletResponse) response, r);
            return false;
        }
        return true;
    }

}

