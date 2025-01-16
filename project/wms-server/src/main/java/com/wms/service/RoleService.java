package com.wms.service;

import com.wms.dto.RoleDTO;
import com.wms.dto.RolePermissionsDTO;
import com.wms.pojo.Role;
import com.wms.service.base.BaseService;
import com.wms.utils.R;
import com.wms.vo.RoleVo;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<Role, RoleVo> {

    Role insertOrUpdate(Role member);

    String lastCode();

    void deleteRole(Long[] ids);

    Map<Integer, String> getPermissionsByRoleId(Long id);

    List<Long> getRoleByMemberId(Long id);

    List<RoleDTO> getAllRole();

    R<?> updateRolePermissions(RolePermissionsDTO rolePermissionsDTO);
}
