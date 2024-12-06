package com.wms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.pojo.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberDao extends BaseMapper<Member> {
}
