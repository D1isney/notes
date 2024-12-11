package com.wms.service.impl;

import com.wms.dao.PermissionsDao;
import com.wms.pojo.Permissions;
import com.wms.service.PermissionsService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.vo.PermissionsVo;
import org.springframework.stereotype.Service;

@Service
public class PermissionsServiceImpl extends IBaseServiceImpl<PermissionsDao, Permissions, PermissionsVo> implements PermissionsService {
    @Override
    public Permissions insertOrUpdate(Permissions member) {
        return null;
    }
}
