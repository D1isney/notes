package com.wms.service;

import com.wms.filter.login.Member;
import com.wms.service.base.BaseService;
import com.wms.utils.R;
import com.wms.vo.MemberVo;

import java.util.Map;

public interface MemberService extends BaseService<Member, MemberVo> {
    R<?> login(Member member);

    R<?> constraintLogin(Member member);

    boolean saveMemberDetails(Member member);

    void logout();

    boolean insertOrSave(Member member);

    R<?> getInfo(String token);

    R<?> getList(Map<String,Object> params);

}
