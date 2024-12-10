package com.wms.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.pojo.Permissions;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionsDao extends BaseMapper<Permissions> {
}
