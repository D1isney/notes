package com.wms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通过此角色来保存角色的权限
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionsDTO implements Serializable {
    //  角色ID
    private Long roleId;
    //  该角色有权限
    private Long[] permissionId;
}
