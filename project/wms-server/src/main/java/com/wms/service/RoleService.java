package com.wms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.pojo.Role;
import com.wms.service.base.BaseService;
import com.wms.vo.RoleVo;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<Role, RoleVo> {

    Role insertOrUpdate(Role member);

    String lastCode();

    void deleteRole(Long[] ids);

    Map<Integer, String> getPermissionsByRoleId(Long id);
}
