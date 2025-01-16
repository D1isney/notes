package com.wms.dao;

import com.wms.pojo.MemberRole;
import com.wms.service.base.IBaseMapper;
import com.wms.vo.MemberRoleVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberRoleDao extends IBaseMapper<MemberRole, MemberRoleVo> {
}
