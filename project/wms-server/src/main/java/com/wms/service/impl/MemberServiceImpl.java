package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.constant.MemberConstant;
import com.wms.dao.MemberDao;
import com.wms.filter.login.PasswordEncoderForSalt;
import com.wms.pojo.LoginMember;
import com.wms.pojo.Member;
import com.wms.service.MemberService;
import com.wms.thread.MemberThreadLocal;
import com.wms.utils.JwtUtil;
import com.wms.utils.PasswordUtil;
import com.wms.utils.R;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberDao, Member> implements MemberService {

    @Resource
    private MemberDao memberDao;
    @Resource
    private PasswordEncoderForSalt passwordEncoder;

    @Lazy
    @Resource
    private AuthenticationManager authenticationManager;


    @Override
    public R<?> login(Member member) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", member.getUsername());
        List<Member> members = memberDao.selectList(queryWrapper);
        if (!members.isEmpty()) {
            String password = member.getPassword() + members.get(0).getSalt();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getUsername(), password);
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            //  如果认证没通过，给出对应的提示
            if (Objects.isNull(authenticate)) {
                throw new RuntimeException("登录失败");
            }
            //  如果认证通过了，使用userId生成一个jwt，jwt存入ResponseResult返回
            LoginMember loginMember = (LoginMember) authenticate.getPrincipal();
            String userId = loginMember.getMember().getId().toString();
            String jwt = JwtUtil.createJWT(userId);
            return R.ok("登录成功！", jwt);

        }
        return R.error("账号或密码错误！");
    }

    @Override
    public boolean saveMemberDetails(Member member) {
        member = createMember(member);
        return member != null;
    }

    public Member createMember(Member member) {
        String username = member.getUsername();
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<Member> members = memberDao.selectList(queryWrapper);
        //  判断该用户是否存在
        if (members.isEmpty()) {
            String salt = PasswordUtil.getSalt();
            //  存盐
            member.setSalt(salt);
            //  将密码加密
            member.setPassword(passwordEncoder.encode(member.getPassword() + salt));
            member.setStatus(MemberConstant.STATUS_TRUE);
            member.setCreateTime(new Date());
            member.setUpdateTime(new Date());
            //  过期时间、默认先给7天
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            Date newDate = calendar.getTime();
            member.setExpirationTime(newDate);

            //  保存数据库
            saveOrUpdate(member);
            return member;
        } else {
            return null;
        }

    }
}
