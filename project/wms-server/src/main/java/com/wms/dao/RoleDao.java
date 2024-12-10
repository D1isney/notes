package com.wms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.pojo.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleDao extends BaseMapper<Role> {
}
