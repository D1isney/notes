package com.wms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.dao.RolePermissionsDao;
import com.wms.pojo.RolePermissions;
import com.wms.service.RolePermissionsService;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionsServiceImpl extends ServiceImpl<RolePermissionsDao, RolePermissions> implements RolePermissionsService {
}
