package com.wms.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wms.dao.RoleDao;
import com.wms.pojo.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {
}
