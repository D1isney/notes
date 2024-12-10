package com.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.filter.login.Member;
import com.wms.utils.R;

public interface MemberService extends IService<Member> {
    R<?> login(Member member);

    boolean saveMemberDetails(Member member);

    void logout();
}
