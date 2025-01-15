package com.wms.service;

import com.wms.filter.login.Member;
import com.wms.pojo.Permissions;
import com.wms.service.base.BaseService;
import com.wms.vo.PermissionsVo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PermissionsService extends BaseService<Permissions, PermissionsVo> {
    void insertOrUpdate(Permissions member);

    List<String> getRemark();

    void deletePermissionsByIds(Long[] ids);

    String lastCode();

    void restPermissions(String token);

    List<Permissions> getPermissionsByRoleId(Long id);

}
