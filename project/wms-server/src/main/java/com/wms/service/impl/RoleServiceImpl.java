package com.wms.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.wms.constant.RoleConstant;
import com.wms.dao.RoleDao;
import com.wms.dto.RoleDTO;
import com.wms.dto.RolePermissionsDTO;
import com.wms.exception.EException;
import com.wms.helper.CurrentHelper;
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
import com.wms.utils.R;
import com.wms.vo.RoleVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RoleServiceImpl extends IBaseServiceImpl<RoleDao, Role, RoleVo> implements RoleService {

    @Resource
    @Lazy
    private RoleDao roleDao;
    @Resource
    @Lazy
    private RolePermissionsService rolePermissionsService;
    @Resource
    @Lazy
    private PermissionsService permissionsService;
    @Resource
    @Lazy
    private MemberRoleService memberRoleService;
    @Resource
    @Lazy
    private Cache<String, Object> cache;

    /**
     * 添加或者修改
     *
     * @param role 角色
     * @return 添加完成的角色
     */
    @Override
    public Role insertOrUpdate(Role role) {
        Role newRole;
        if (checkRoleNewOrOld(role)) {
            newRole = createRole(role);
        } else {
            newRole = saveOrModify(role);
        }
        //  清楚所有缓存
        cache.invalidateAll();
        return newRole;
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

    @Value("${cache.permissions-key}")
    private String permissionsKey;
    @Resource
    private CurrentHelper currentHelper;

    /**
     * 通过角色ID以及权限ID来修改角色的权限
     *
     * @param rolePermissionsDTO DTO
     * @return R<?>
     */
    @Override
    public R<?> updateRolePermissions(RolePermissionsDTO rolePermissionsDTO) {
        //  检查参数的有效性
        checkRolePermissions(rolePermissionsDTO);
        Long roleId = rolePermissionsDTO.getRoleId();
        List<Permissions> permissionsByRoleIdList = permissionsService.getPermissionsByRoleId(roleId);
        //  新的全部的权限id
        Long[] permissionId = rolePermissionsDTO.getPermissionId();
        Set<Long> permissionIdSet = new HashSet<>(Arrays.asList(permissionId));
        Long currentMember = getCurrentMember();
        if (!Objects.isNull(permissionsByRoleIdList)) {
            withPermission(permissionIdSet, permissionsByRoleIdList, roleId, currentMember);
        } else {
            unauthorized(permissionIdSet, roleId, currentMember);
        }
        //  清空所有的缓存
        cache.invalidate(permissionsKey + currentHelper.getCurrentMemberId());
        return R.ok("修改成功！");
    }

    /**
     * 角色没有权限的操作
     *
     * @param permissionIdSet 前端传回来的权限ID
     * @param roleId          角色ID
     * @param currentMember   当前线程的用户
     */
    public void unauthorized(Set<Long> permissionIdSet, Long roleId, Long currentMember) {
        //  说明原本角色什么权限都没有，直接把所有权限都加进去
        List<RolePermissions> list = new ArrayList<>();
        permissionIdSet.forEach(permissionsId -> {
            RolePermissions rolePermissions = createRolePermissions(roleId, permissionsId, currentMember);
            list.add(rolePermissions);
        });
        rolePermissionsService.saveOrUpdateBatch(list);
    }

    /**
     * 角色有权限的操作
     *
     * @param permissionIdSet         前端传回来的权限id，这里是已经选中了的
     * @param permissionsByRoleIdList 本来该角色拥有的权限
     * @param roleId                  角色ID
     * @param currentMember           当前线程的用户
     */
    public void withPermission(Set<Long> permissionIdSet, List<Permissions> permissionsByRoleIdList, Long roleId, Long currentMember) {
        //  删除的
        List<Permissions> deletePermissions = new ArrayList<>();
        //  新添加的
        Set<Long> addPermissions = new HashSet<>(permissionIdSet); // 初始化为所有期望的权限ID
        // 分类
        permissionsByRoleIdList.forEach(permission -> {
            Long permId = permission.getId();
            if (permissionIdSet.contains(permId)) {
                addPermissions.remove(permId);
            } else {
                deletePermissions.add(permission);
            }
        });
        //  删除m没有了的权限
        List<RolePermissions> deleteRolePermissions = new ArrayList<>();
        deletePermissions.forEach(permission -> {
            RolePermissions rolePermissions = queryRolePermissions(roleId, permission.getId());
            deleteRolePermissions.add(rolePermissions);
        });
        if (!deleteRolePermissions.isEmpty())
            rolePermissionsService.deleteByIds(deleteRolePermissions.stream().map(RolePermissions::getId).toArray(Long[]::new));
        //  新添加的权限
        List<RolePermissions> addRolePermissions = new ArrayList<>();
        addPermissions.forEach(permissionsId -> {
            RolePermissions rolePermissions = createRolePermissions(roleId, permissionsId, currentMember);
            addRolePermissions.add(rolePermissions);
        });
        if (!addRolePermissions.isEmpty()) rolePermissionsService.saveOrUpdateBatch(addRolePermissions);

    }


    /**
     * 通过角色ID以及权限ID找到对应的权限对应表
     *
     * @param roleId        角色ID
     * @param permissionsId 权限ID
     * @return 角色权限关系
     */
    public RolePermissions queryRolePermissions(Long roleId, Long permissionsId) {
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", roleId);
        map.put("permissionsId", permissionsId);
        List<RolePermissions> rolePermissions = rolePermissionsService.queryList(map);
        if (Objects.isNull(rolePermissions)) {
            throw new EException("请检查该权限是否存在！");
        }
        return rolePermissions.get(0);
    }


    /**
     * 创建一个新的角色权限关系
     *
     * @param roleId        角色ID
     * @param permissionsId 权限ID
     * @param currentMember 当前线程的用户
     * @return 新的角色权限关系
     */
    private RolePermissions createRolePermissions(Long roleId, Long permissionsId, Long currentMember) {
        RolePermissions rolePermissions = new RolePermissions();
        rolePermissions.setRoleId(roleId);
        rolePermissions.setPermissionsId(permissionsId);
        rolePermissions.setCreateTime(new Date());
        rolePermissions.setUpdateTime(new Date());
        rolePermissions.setCreateMember(currentMember);
        rolePermissions.setUpdateMember(currentMember);
        return rolePermissions;
    }

    /**
     * 检查传进来的参数时候有用
     *
     * @param rolePermissionsDTO DTO
     */
    public void checkRolePermissions(RolePermissionsDTO rolePermissionsDTO) {
        if (Objects.isNull(rolePermissionsDTO)) {
            throw new EException("rolePermissionsDTO参数为空！");
        } else if (Objects.isNull(rolePermissionsDTO.getRoleId())) {
            throw new EException("角色为空！");
        }
    }


}
