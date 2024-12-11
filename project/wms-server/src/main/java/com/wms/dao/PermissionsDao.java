package com.wms.dao;

import com.wms.pojo.Permissions;
import com.wms.service.base.IBaseMapper;
import com.wms.vo.PermissionsVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionsDao extends IBaseMapper<Permissions, PermissionsVo> {
}
