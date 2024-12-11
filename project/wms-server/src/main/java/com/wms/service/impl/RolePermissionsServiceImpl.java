package com.wms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.dao.RolePermissionsDao;
import com.wms.pojo.RolePermissions;
import com.wms.service.RolePermissionsService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.vo.RolePermissionsVo;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionsServiceImpl extends IBaseServiceImpl<RolePermissionsDao, RolePermissions, RolePermissionsVo> implements RolePermissionsService  {

    @Override
    public RolePermissions insertOrUpdate(RolePermissions rolePermissions) {
        return saveOrModify(rolePermissions);
    }
}
