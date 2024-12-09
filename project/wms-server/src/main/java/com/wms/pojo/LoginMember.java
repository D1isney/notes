package com.wms.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.wms.constant.MemberConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class LoginMember implements UserDetails {
    private Member member;
    //  权限
    private List<String> menu;

    //  优化、不需要把这个东西序列化存储到数据库
    @JSONField(serialize = false)
    List<SimpleGrantedAuthority> authorities;

    public LoginMember(Member member, List<String> menu) {
        this.member = member;
        this.menu = menu;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //  优化、做成成员变量
        if (authorities != null) {
            return authorities;
        }
        authorities = menu
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return checkExpirationTime(member);
    }

    @Override
    public boolean isAccountNonLocked() {
        return checkExpirationTime(member);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return checkStatus(member);
    }


    /**
     * 账号是否可用
     *
     * @param member 用户
     * @return true可用 false 不可用
     */
    public boolean checkStatus(Member member) {
        int status = member.getStatus();
        return status == MemberConstant.STATUS_TRUE;
    }

    public boolean checkExpirationTime(Member member) {
        Date expirationTime = member.getExpirationTime();
        Date now = new Date();
        int i = expirationTime.compareTo(now);
        return i >= 0;
    }

}
