package com.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.pojo.Role;
import com.wms.service.base.BaseService;
import com.wms.vo.RoleVo;

public interface RoleService extends BaseService<Role, RoleVo> {

    Role insertOrUpdate(Role member);
}
