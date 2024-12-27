package com.wms.service.impl;

import com.wms.constant.RoleConstant;
import com.wms.dao.RoleDao;
import com.wms.dto.RoleDTO;
import com.wms.exception.EException;
import com.wms.pojo.MemberRole;
import com.wms.pojo.Permissions;
import com.wms.pojo.Role;
import com.wms.pojo.RolePermissions;
import com.wms.service.MemberRoleService;
import com.wms.service.PermissionsService;
import com.wms.service.RolePermissionsService;
import com.wms.service.RoleService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.thread.MemberThreadLocal;
import com.wms.utils.CodeUtils;
import com.wms.utils.DateConstant;
import com.wms.vo.RoleVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RoleServiceImpl extends IBaseServiceImpl<RoleDao, Role, RoleVo> implements RoleService {

    @Resource
    private RoleDao roleDao;
    @Resource
    private RolePermissionsService rolePermissionsService;
    @Resource
    private PermissionsService permissionsService;
    @Resource
    private MemberRoleService memberRoleService;

    /**
     * 添加或者修改
     *
     * @param role 角色
     * @return 添加完成的角色
     */
    @Override
    public Role insertOrUpdate(Role role) {
        if (checkRoleNewOrOld(role)) {
            return createRole(role);
        } else {
            return saveOrModify(role);
        }
    }


    /**
     * 创建角色
     *
     * @param role 前端传过来的角色
     */
    public Role createRole(Role role) {
        checkRoleNameIsExist(role);
        role.setCode(lastCode());
        role.setCreateMember(getCurrentMember());
        role.setUpdateMember(getCurrentMember());
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        if (Objects.isNull(role.getStatus())) {
            role.setStatus(RoleConstant.USABLE);
        }
        role = saveOrModify(role);
        //  给这个角色添加默认的权限
        boolean defaultRolePermissions = rolePermissionsService.createDefaultRolePermissions(role);
        if (!defaultRolePermissions) {
            throw new EException("默认权限添加失败！");
        }
        return role;
    }

    public Long getCurrentMember() {
        return MemberThreadLocal.get().getMember().getId();
    }


    /**
     * 检查是新添加的还是旧添加的Role
     *
     * @param role 角色
     * @return true 新添加的、false 旧的
     */
    public boolean checkRoleNewOrOld(Role role) {
        return Objects.isNull(role.getId());
    }

    /**
     * 检查该角色名是否已经存在了
     *
     * @param role 角色
     */
    public void checkRoleNameIsExist(Role role) {
        Map<String, Object> map = new HashMap<>();
        if (Objects.isNull(role.getName())) {
            throw new EException("传入无效角色名！");
        }
        map.put("name", role.getName());
        List<Role> roles = queryList(map);
        if (!roles.isEmpty()) {
            throw new EException("该角色已存在，请修改后重新添加！");
        }

    }


    @Override
    public String lastCode() {
        return CodeUtils.getString(roleDao.lastCode());
    }


    /**
     * 删除角色、以及这个角色与权限的关系
     *
     * @param ids 角色id
     */
    @Override
    public void deleteRole(Long[] ids) {
        //  通过角色ID找到所有的角色权限关系
        Map<String, Object> map = new HashMap<>();
        Arrays.stream(ids).forEach(id -> {
            map.put("role_id", id);
            List<RolePermissions> rolePermissions = rolePermissionsService.queryList(map);
            if (!rolePermissions.isEmpty()) {
                //  删除掉所有关系
                Long[] array = rolePermissions.stream().map(RolePermissions::getId).toArray(Long[]::new);
                rolePermissionsService.deleteByIds(array);
            }
        });
        //  删掉选中的ids
        deleteByIds(ids);
    }

    //  根据角色ID 找到所有的权限信息
    @Override
    public Map<Integer, String> getPermissionsByRoleId(Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("role_id", id);
        List<RolePermissions> rolePermissions = rolePermissionsService.queryList(map);
        Map<Integer, String> result = new HashMap<>();
        AtomicInteger i = new AtomicInteger(1);
        if (!rolePermissions.isEmpty()) {
            rolePermissions.forEach(rolePermission -> {
                Long permissionsId = rolePermission.getPermissionsId();
                Permissions permissions = permissionsService.queryById(permissionsId);
                if (!Objects.isNull(permissions)) {
                    String key = "权限：" + i.get();
                    result.put(i.get(), key + "（" + permissions.getName() + "）");
                    i.getAndIncrement(); // i++
                }
            });
        }
        return result;
    }

    /**
     * 通过用户ID找到该用户拥有的角色
     *
     * @param id 用户ID
     * @return 拥有的角色信息
     */
    @Override
    public List<Long> getRoleByMemberId(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", id);
        List<MemberRole> memberRolesList = memberRoleService.queryList(map);
        if (!memberRolesList.isEmpty()) {
            List<Long> ids = new ArrayList<>();
            memberRolesList.forEach(memberRole -> {
                Long roleId = memberRole.getRoleId();
//                Role role = queryById(roleId);
//                RoleDTO roleDTO = new RoleDTO();
//                roleDTO.setLabel(role.getName());
//                roleDTO.setValue(role.getId());
                ids.add(roleId);

            });
            return ids;
        }
        return null;
    }

    /**
     * 拿到所有的角色
     *
     * @return 角色信息
     */
    @Override
    public List<RoleDTO> getAllRole() {
        List<Role> roleList = list();
        if (!roleList.isEmpty()) {
            List<RoleDTO> list = new ArrayList<>();
            roleList.forEach(role -> {
                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setLabel(role.getName());
                roleDTO.setValue(role.getId());
                list.add(roleDTO);
            });
            return list;
        }
        return null;
    }


}
