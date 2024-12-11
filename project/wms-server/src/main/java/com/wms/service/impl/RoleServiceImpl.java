package com.wms.service.impl;

import com.wms.dao.RoleDao;
import com.wms.pojo.Role;
import com.wms.service.RoleService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.vo.RoleVo;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends IBaseServiceImpl<RoleDao, Role, RoleVo> implements RoleService {
    @Override
    public Role insertOrUpdate(Role role) {
        return saveOrModify(role);
    }
}
