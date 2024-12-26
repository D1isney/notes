package com.wms.dao;

import com.wms.pojo.Role;
import com.wms.service.base.IBaseMapper;
import com.wms.vo.RoleVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleDao extends IBaseMapper<Role, RoleVo> {

    String lastCode();
}
