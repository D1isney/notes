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
import com.wms.utils.HttpUtil;
import com.wms.utils.JwtUtil;
import com.wms.utils.PasswordUtil;
import com.wms.utils.R;
import com.wms.vo.MemberVo;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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


    @Override
    public R<?> login(Member member) {
        List<Member> members = queryMemberByUsername(member);
        if (!members.isEmpty()) {
            //  判断用户是否登录过
            R<?> login = isLogin(members.get(0).getId());
            if (!Objects.isNull(login)) {
                return login;
            }

            String jwt = loggingIn(member, members);
            return R.ok("登录成功！", jwt);
        }
        return R.error("账号或密码错误！");
    }

    /**
     * 强制登录
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

    public String loggingIn(Member member,List<Member> members){
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
        if (Objects.isNull(member.getUsername())){
            throw new EException("用户名不能为空！");
        }
    }
    public List<Member> queryMemberByUsername(Member member){
        checkMember(member);
        Map<String,Object> map = new HashMap<>();
        map.put("username", member.getUsername());
        return queryList(map);
    }



    /**
     * 判断用户是否已经登录了
     * @param id 用户id
     * @return 有值：登陆过 null：没有登陆过
     */
    public R<?> isLogin(Long id)  {
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
        member = createMember(member);
        return member != null;
    }

    @Override
    public void logout() {
        Long id = MemberThreadLocal.get().getMember().getId();
        //  更新用户的信息
        Member byId = getById(id);
        byId.setStatus(MemberConstant.STATUS_FALSE);
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
    public boolean insertOrSave(Member member) {
        if (Objects.isNull(member)) {
            return false;
        } else {
            boolean b = checkNewOrOldMember(member);
            //  说明是新的用户
            if (b) {
                createMember(member);
            } else {
                //  旧的用户
                save(member);
            }
        }
        return true;
    }



    public boolean checkNewOrOldMember(Member member) {
        return member.getId() == null;
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
            save(member);
            return member;
        } else {
            return null;
        }

    }
}
