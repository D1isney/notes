package com.wms.service.impl;

import com.wms.dao.RolePermissionsDao;
import com.wms.dto.DefaultPermissionsDTO;
import com.wms.pojo.Permissions;
import com.wms.pojo.Role;
import com.wms.pojo.RolePermissions;
import com.wms.service.PermissionsService;
import com.wms.service.RolePermissionsService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.thread.MemberThreadLocal;
import com.wms.vo.RolePermissionsVo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RolePermissionsServiceImpl extends IBaseServiceImpl<RolePermissionsDao, RolePermissions, RolePermissionsVo> implements RolePermissionsService {

    @Resource
    @Lazy
    private PermissionsService permissionsService;

    @Override
    public RolePermissions insertOrUpdate(RolePermissions rolePermissions) {
        return saveOrModify(rolePermissions);
    }


    /**
     * 默认权限
     *
     * @param role 需要给权限的角色
     * @return 添加成功与否
     */
    @Override
    public boolean createDefaultRolePermissions(Role role) {
        List<String> permissionsDTO = DefaultPermissionsDTO.getDefaultPermissions();
        if (permissionsDTO.isEmpty()) {
            return false;
        } else {
            Long roleId = role.getId();
            Map<String, Object> map = new HashMap<>();
            List<RolePermissions> list = new ArrayList<>();
            permissionsDTO.forEach(p -> {
                map.put("path",p);
                //  找到对应的权限
                List<Permissions> permissions = permissionsService.queryList(map);
                if (!permissions.isEmpty()) {
                    Permissions permission = permissions.get(0);
                    RolePermissions rolePermissions = getRolePermissions(roleId, permission);
                    list.add(rolePermissions);
                }
            });
            if (!list.isEmpty()) {
                saveOrUpdateBatch(list);
            }
            return true;
        }
    }

    /**
     *
     * @param roleId 角色ID
     * @param permission 拿到的权限
     * @return 角色权限表
     */
    private RolePermissions getRolePermissions(Long roleId, Permissions permission) {
        RolePermissions rolePermissions = new RolePermissions();
        rolePermissions.setRoleId(roleId);
        rolePermissions.setPermissionsId(permission.getId());
        rolePermissions.setCreateMember(getCurrentMember());
        rolePermissions.setUpdateMember(getCurrentMember());
        rolePermissions.setCreateTime(new Date());
        rolePermissions.setUpdateTime(new Date());
        rolePermissions.setRemark(" ");
        return rolePermissions;
    }

    public Long getCurrentMember(){
        return MemberThreadLocal.get().getMember().getId();
    }

}
