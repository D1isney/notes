package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wms.dao.MemberDao;
import com.wms.filter.login.LoginMember;
import com.wms.filter.login.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class MemberDetailsServiceImpl implements UserDetailsService {
    @Resource
    private MemberDao memberDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<Member> members = memberDao.selectList(queryWrapper);
        Member member = null;
        if (!members.isEmpty()) {
            member = members.get(0);
            //  通过用户去找权限
        }
        List<String> list = Collections.singletonList("test");
        return new LoginMember(member,list);
    }
}
