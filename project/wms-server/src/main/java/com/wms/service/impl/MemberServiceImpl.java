package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wms.constant.MemberConstant;
import com.wms.dao.MemberDao;
import com.wms.exception.EException;
import com.wms.filter.login.PasswordEncoderForSalt;
import com.wms.filter.login.LoginMember;
import com.wms.filter.login.Member;
import com.wms.service.MemberService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.thread.MemberThreadLocal;
import com.wms.utils.*;
import com.wms.vo.MemberVo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static com.wms.constant.MemberConstant.CONTINUE_LOGIN;

@Service
public class MemberServiceImpl extends IBaseServiceImpl<MemberDao, Member, MemberVo> implements MemberService {

    @Resource
    private MemberDao memberDao;
    @Resource
    private PasswordEncoderForSalt passwordEncoder;

    @Lazy
    @Resource
    private AuthenticationManager authenticationManager;

    @Value("${login.filter.isLogin}")
    private Integer isLogin;

    @Override
    public R<?> login(Member member) {
        List<Member> members = queryMemberByUsername(member);
        if (!members.isEmpty()) {
            //  判断用户是否登录过
            if (isLogin > 0) {
                R<?> login = isLogin(members.get(0).getId());
                if (!Objects.isNull(login)) {
                    return login;
                }
            }
            String jwt = loggingIn(member, members);
            return R.ok("登录成功！", jwt);
        }
        return R.error("账号或密码错误！");
    }

    /**
     * 强制登录
     *
     * @param member 用户
     * @return R
     */
    @Override
    public R<?> constraintLogin(Member member) {
        List<Member> members = queryMemberByUsername(member);
        if (!members.isEmpty()) {
            String jwt = loggingIn(member, members);
            return R.ok("登录成功！", jwt);
        }
        return R.error("账号或密码错误！");
    }

    public String loggingIn(Member member, List<Member> members) {
        String password = member.getPassword() + members.get(0).getSalt();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getUsername(), password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //  如果认证没通过，给出对应的提示
        if (Objects.isNull(authenticate)) {
            throw new EException("登录失败");
        }
        LoginMember loginMember = (LoginMember) authenticate.getPrincipal();
        String userId = loginMember.getMember().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        MemberThreadLocal.setMainThreadLoginMemberById(members.get(0).getId(), loginMember);
        MemberThreadLocal.setMainThreadLoginMemberTokenForId(members.get(0).getId(), jwt);
        members.get(0).setStatus(MemberConstant.IS_ONLINE);
        saveOrModify(members.get(0));
        return jwt;
    }

    public void checkMember(Member member) {
        if (Objects.isNull(member.getUsername())) {
            throw new EException("用户名不能为空！");
        }
    }

    public List<Member> queryMemberByUsername(Member member) {
        checkMember(member);
        Map<String, Object> map = new HashMap<>();
        map.put("username", member.getUsername());
        return queryList(map);
    }


    /**
     * 判断用户是否已经登录了
     *
     * @param id 用户id
     * @return 有值：登陆过 null：没有登陆过
     */
    public R<?> isLogin(Long id) {
        //  校验用户是否登陆过
        if (null != MemberThreadLocal.getMemberInfoMap(id)) {
            R<Object> r = R.ok("已经有人登录，是否继续登录？");
            r.setCode(CONTINUE_LOGIN);
            return r;
        }
        return null;
    }


    @Override
    public boolean saveMemberDetails(Member member) {
        Long id = 0L;
        try {
            id = MemberThreadLocal.get().getMember().getId();
        } catch (Exception e) {
            id = 1L;
        }
        member = createMember(member, id);
        return member != null;
    }

    @Override
    public void logout() {
        Long id = MemberThreadLocal.get().getMember().getId();
        //  更新用户的信息
        Member byId = getById(id);
        byId.setOnline(MemberConstant.NOT_ONLINE);
        updateById(byId);
        //  清楚登录信息
        MemberThreadLocal.clearMainThreadLoginMemberById(MemberThreadLocal.get().getMember().getId());
        //  清掉Token
        MemberThreadLocal.clearMainThreadLoginMemberTokenForId(MemberThreadLocal.get().getMember().getId());
        //  清楚当前线程的用户信息
        MemberThreadLocal.clear();
    }

    /**
     * 保存或者查询一个新的用户
     *
     * @param member 用户
     * @return true 插入成功 false 插入失败
     */
    @Override
    public R<?> insertOrSave(Member member) {
        if (Objects.isNull(member)) {
            return R.error("请求参数为空！");
        } else {
            boolean b = checkNewOrOldMember(member);
            Long currentMemberId = MemberThreadLocal.get().getMember().getId();
            //  说明是新的用户
            if (b) {
                createMember(member, currentMemberId);
                return R.ok("添加成功！");
            } else {
                //  更新人
                member.setUpdateMember(currentMemberId);
                //  旧的用户
                saveOrModify(member);
                return R.ok("修改成功！");
            }
        }
    }

    @Override
    public R<?> getInfo(String token) {
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            throw new EException("用户Token解析失败");
        }
        LoginMember memberInfoMap = MemberThreadLocal.getMemberInfoMap(Long.valueOf(userid));
        if (memberInfoMap == null) {
            return R.error("用户还未登录，请重新登录！", null);
        }
        memberInfoMap.getMember().setSalt("******");
        memberInfoMap.getMember().setPassword("******");
        return R.ok("", memberInfoMap.getMember());
    }


    public boolean checkNewOrOldMember(Member member) {
        return Objects.isNull(member.getId());
    }


    public Member createMember(Member member, Long currentMemberId) {
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


            member.setCreateMember(currentMemberId);
            member.setUpdateMember(currentMemberId);
            member.setOnline(MemberConstant.NOT_ONLINE);

            //  保存数据库
            saveOrModify(member);
            return member;
        } else {
            throw new EException("该用户已存在，请修改Username后重新保存！");
        }

    }
}
