package com.wms.service.impl;

import com.wms.dao.PermissionsDao;
import com.wms.dto.DefaultPermissionsDTO;
import com.wms.dto.RestDTO;
import com.wms.exception.EException;
import com.wms.filter.login.Member;
import com.wms.pojo.LogRecord;
import com.wms.pojo.Permissions;
import com.wms.pojo.Role;
import com.wms.pojo.RolePermissions;
import com.wms.service.*;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.thread.MemberThreadLocal;
import com.wms.utils.CodeUtils;
import com.wms.utils.DateConstant;
import com.wms.utils.JwtUtil;
import com.wms.vo.PermissionsVo;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.wms.enums.LogRecordEnum.ALARM_LOG;

@Service
public class PermissionsServiceImpl extends IBaseServiceImpl<PermissionsDao, Permissions, PermissionsVo> implements PermissionsService {

    @Resource
    private PermissionsDao permissionsDao;


    @Resource
    @Lazy
    private RolePermissionsService rolePermissionsService;
    @Resource
    @Lazy
    private MemberService memberService;
    @Resource
    @Lazy
    private RoleService roleService;

    @Resource
    @Lazy
    private MemberRoleService memberRoleService;

    @Resource
    @Lazy
    private LogRecordService logRecordService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertOrUpdate(Permissions permissions) {
        if (Objects.isNull(permissions.getId())) {
            Map<String, Object> map = new HashMap<>();
            map.put("path", permissions.getPath());
            List<Permissions> list = queryList(map);
            if (!list.isEmpty()) {
                throw new EException("该权限" + permissions.getPath() + "已存在，请修改后重新保存！");
            }
            //  说明是新的权限
            permissions.setCode(lastCode());
            permissions.setCreateTime(new Date());
            permissions.setUpdateTime(new Date());
            permissions.setCreateMember(getCurrentMemberId());
            permissions.setUpdateMember(getCurrentMemberId());
        } else {
            permissions.setUpdateTime(new Date());
            permissions.setUpdateMember(getCurrentMemberId());
        }
        saveOrModify(permissions);
    }

    public Long getCurrentMemberId() {
        return MemberThreadLocal.get().getMember().getId();
    }


    @Override
    public List<String> getRemark() {
        return permissionsDao.getRemark();
    }


    @Override
    public void deletePermissionsByIds(Long[] ids) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Long id : ids) {
            map.put("permissions_id", id);
            List<RolePermissions> rolePermissions = rolePermissionsService.queryList(map);
            if (!rolePermissions.isEmpty()) {
                rolePermissions.forEach(rolePermission -> {
                    rolePermissionsService.delete(rolePermission.getId());
                });
            }
        }
        deleteByIds(ids);
    }

    @Override
    public String lastCode() {
        return CodeUtils.getString(permissionsDao.lastCode());
    }


    /**
     * 刷新所有的用户信息
     */
    @Override
    public void restPermissions(String token) {
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            throw new EException("用户Token解析失败！");
        }
        Long id = Long.parseLong(userid);
        Member member = memberService.queryById(id);
        if (Objects.isNull(member)) {
            throw new EException("用户Token解析失败！");
        }
        //  记录日志
        createLog(member);

        //  清空RBAC五张表
        rest();


        RestDTO dto = DefaultPermissionsDTO.getRestPermissions();
        Member restMember = dto.getMember();
        Role restRole = dto.getRole();
        List<Permissions> restPermissionsList = dto.getPermissions();


        restMember = createMember(restMember);
    }

    public Member createMember(Member member) {
        member.setCreateTime(new Date());
        member.setUpdateTime(new Date());


        return member;
    }

    public void rest(){
        clearRolePermissions();
        clearMemberRole();
        clearPermissions();
        clearRole();
        clearMember();

    }


    private void clearPermissions() {
        list().forEach(permissions -> {
            delete(permissions.getId());
        });
    }
    private void clearRole() {
        roleService.list().forEach(role -> {
            roleService.delete(role.getId());
        });
    }
    public void clearMemberRole() {
        memberRoleService.list().forEach(memberRole -> {
            memberRoleService.delete(memberRole.getId());
        });
    }
    public void clearMember() {
        memberService.list().forEach(member -> {
            memberService.delete(member.getId());
        });
    }
    public void clearRolePermissions() {
        rolePermissionsService.list().forEach(rolePermission -> {
            rolePermissionsService.delete(rolePermission.getId());
        });
    }

    /**
     * 记录日志
     * @param member 刷新的用户
     */
    public void createLog(Member member) {
        LogRecord logRecord = new LogRecord();
        //  报警日志
        logRecord.setType(ALARM_LOG.getCode());
        logRecord.setMemberId(member.getId());
        logRecord.setCreateTime(new Date());
        logRecord.setMessage("Username：" + member.getUsername() +
                "，Name：" + member.getName() +
                ",ID：" + member.getId() +
                "，调用接口：/permissions/restPermissions"
        );
        logRecord.setPath("/permissions/restPermissions");
        logRecord.setResult("所有用户、角色、权限被刷新！！！！");
        logRecord.setExecuteTime(0L);
        logRecord.setParams("NULL");

        logRecordService.saveOrUpdate(logRecord);
    }


}
