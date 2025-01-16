package com.wms.service;

import com.wms.pojo.MemberRole;
import com.wms.service.base.BaseService;
import com.wms.vo.MemberRoleVo;

public interface MemberRoleService extends BaseService<MemberRole, MemberRoleVo> {

    MemberRole insertOrUpdate(MemberRole member);
}
