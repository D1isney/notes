package com.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.pojo.RolePermissions;
import com.wms.service.base.BaseService;
import com.wms.vo.RolePermissionsVo;

public interface RolePermissionsService extends BaseService<RolePermissions, RolePermissionsVo> {

    RolePermissions insertOrUpdate(RolePermissions member);
}
