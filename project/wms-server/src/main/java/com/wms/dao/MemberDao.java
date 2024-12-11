package com.wms.dao;

import com.wms.filter.login.Member;
import com.wms.service.base.IBaseMapper;
import com.wms.vo.MemberVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberDao extends IBaseMapper<Member, MemberVo> {

    List<String> getPermissionsByMember(Long id);
}
