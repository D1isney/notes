package com.wms.dao;

import com.wms.pojo.RolePermissions;
import com.wms.service.base.IBaseMapper;
import com.wms.vo.RolePermissionsVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RolePermissionsDao extends IBaseMapper<RolePermissions, RolePermissionsVo> {
}
