package com.wms.service;

import com.wms.pojo.Role;
import com.wms.pojo.RolePermissions;
import com.wms.service.base.BaseService;
import com.wms.vo.RolePermissionsVo;

public interface RolePermissionsService extends BaseService<RolePermissions, RolePermissionsVo> {

    RolePermissions insertOrUpdate(RolePermissions member);

    boolean createDefaultRolePermissions(Role role);
}
