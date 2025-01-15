package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.wms.constant.MemberConstant;
import com.wms.dao.MemberDao;
import com.wms.exception.EException;
import com.wms.filter.login.PasswordEncoderForSalt;
import com.wms.filter.login.LoginMember;
import com.wms.filter.login.Member;
import com.wms.pojo.MemberRole;
import com.wms.service.MemberRoleService;
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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

import static com.wms.constant.MemberConstant.CONTINUE_LOGIN;
import static com.wms.constant.MemberConstant.GO_ON_LOGIN;

@Service
public class MemberServiceImpl extends IBaseServiceImpl<MemberDao, Member, MemberVo> implements MemberService {

    @Resource
    private MemberDao memberDao;
    @Resource
    private PasswordEncoderForSalt passwordEncoder;

    @Lazy
    @Resource
    private AuthenticationManager authenticationManager;

    @Lazy
    @Resource
    private MemberRoleService memberRoleService;

    @Value("${login.filter.isLogin}")
    private Integer isLogin;

    @Value("${cache.user-key}")
    private String userKey;

    @Resource
    private Cache<String,Object> cache;

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
            cache.put(userKey+members.get(0).getId(),members.get(0));
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
        if (Objects.equals(members.get(0).getStatus(),MemberConstant.STATUS_FALSE)){
            throw new EException("该账号不可使用！请联系管理员！");
        }else if (Objects.equals(members.get(0).getStatus(),MemberConstant.STATUS_PAST)){
            throw new EException("该账号已被封禁！请联系管理员！");
        }
        members.get(0).setOnline(MemberConstant.IS_ONLINE);
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
            r.setCode(GO_ON_LOGIN);
            return r;
        }
        return null;
    }


    @Override
    public boolean saveMemberDetails(Member member) {
        Long id;
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

        //  清除
        cache.invalidate(userKey+byId.getId());
    }

    /**
     * 保存或者查询一个新的用户
     *
     * @param member 用户
     * @return true 插入成功 false 插入失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> insertOrSave(Member member) {
        Member newMember;
        if (Objects.isNull(member)) {
            return R.error("请求参数为空！");
        } else {
            boolean b = checkNewOrOldMember(member);
            Long currentMemberId = MemberThreadLocal.get().getMember().getId();
            //  说明是新的用户
            if (b) {
                newMember = createMember(member, currentMemberId);
                createMemberRole(member, newMember, currentMemberId);
                return R.ok("添加成功！");
            } else {
                //  更新人
                member.setUpdateMember(currentMemberId);
                //  旧的用户
                newMember = saveOrModify(member);
                createMemberRole(member, newMember, currentMemberId);
                //  更新该用户的缓存
                cache.put(userKey+newMember.getId() , newMember);
                return R.ok("修改成功！");
            }
        }
    }

    /**
     * 添加用户和角色的关系
     *
     * @param param     参数
     * @param newMember 新用户或者保存之后的用户
     */
    public void createMemberRole(Member param, Member newMember, Long currentMemberId) {
        if (!Objects.isNull(param.getRoleId())) {
            List<MemberRole> list = new ArrayList<>();
            Long[] roleId = param.getRoleId();
            Long id = newMember.getId();
            for (Long role_id : roleId) {
                Map<String, Object> map = new HashMap<>();
                map.put("member_id", id);
                map.put("role_id", role_id);
                List<MemberRole> memberRoles = memberRoleService.queryList(map);
                if (memberRoles.isEmpty()) {
                    MemberRole memberRole = new MemberRole();
                    memberRole.setMemberId(id);
                    memberRole.setRoleId(role_id);
                    memberRole.setCreateTime(new Date());
                    memberRole.setUpdateTime(new Date());
                    memberRole.setCreateMember(currentMemberId);
                    memberRole.setUpdateMember(currentMemberId);
                    list.add(memberRole);
                } else {
                    List<MemberRole> oldList = memberRoleService.list();
                    oldList.forEach(memberRole -> {
                        //  看看新保存的有没有这个角色，有就更新时间，没有就删除这个关系
                        if (memberRole.getRoleId().equals(role_id)) {
                            memberRole.setUpdateTime(new Date());
                            memberRole.setUpdateMember(currentMemberId);
                            list.add(memberRole);
                        }else{
                            memberRoleService.delete(memberRole);
                        }
                    });
                }
            }
            memberRoleService.saveOrUpdateBatch(list);
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
            return R.error("用户还未登录，请重新登录！", 400);
        }
        memberInfoMap.getMember().setSalt("******");
        memberInfoMap.getMember().setPassword("******");
        return R.ok("", memberInfoMap.getMember());
    }


    @Override
    public void deleteByMemberId(Long[] ids) {
        //  通过id，找到对应的用户角色
        Map<String, Object> map = new HashMap<>();
        for (Long id : ids) {
            map.put("member_id", id);
            List<MemberRole> memberRoles = memberRoleService.queryList(map);
            memberRoles.forEach(memberRole -> memberRoleService.delete(memberRole.getId()));
        }
        deleteByIds(ids);
    }


    public boolean checkNewOrOldMember(Member member) {
        return Objects.isNull(member.getId());
    }


    /**
     * 创建一个新的用户
     * @param member 创建新的用户
     * @param currentMemberId 当前线程的用户
     * @return 新的用户
     */
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
