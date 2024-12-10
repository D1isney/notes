package com.wms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.dao.PermissionsDao;
import com.wms.pojo.Permissions;
import com.wms.service.PermissionsService;
import org.springframework.stereotype.Service;

@Service
public class PermissionsServiceImpl extends ServiceImpl<PermissionsDao, Permissions> implements PermissionsService {
}
