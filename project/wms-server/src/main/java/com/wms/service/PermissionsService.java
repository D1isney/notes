package com.wms.service;

import com.wms.pojo.Permissions;
import com.wms.service.base.BaseService;
import com.wms.vo.PermissionsVo;

public interface PermissionsService extends BaseService<Permissions, PermissionsVo> {
    Permissions insertOrUpdate(Permissions member);
}
