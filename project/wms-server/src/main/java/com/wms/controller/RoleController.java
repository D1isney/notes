package com.wms.controller;


import com.wms.aspect.Log;
import com.wms.pojo.Role;
import com.wms.utils.PageUtil;
import com.wms.utils.Query;
import com.wms.utils.R;
import com.wms.vo.RoleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import com.wms.service.RoleService;

import javax.annotation.Resource;


/**
 * <p>
 * controller
 * </p>
 */
@Api(tags = "角色", value = "/role")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @ApiOperation("查询数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = " ", value = " ")
    })
    @GetMapping("list")
    @Log(value = "角色-查询所有角色信息", path = "/role/list")
    public R<?> list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        IPage<RoleVo> page = roleService.pageList(query.getIPage(RoleVo.class), query);
        PageUtil pageUtil = new PageUtil(page.getRecords(), page.getTotal(), query.getLimit(), query.getPage());
        return R.ok(pageUtil);
    }


    @ApiOperation("新增或者修改")
    @ApiImplicitParam(name = "Role", value = "role")
    @PostMapping("saveOrUpdate")
    @Log(value = "角色-修改保存角色信息", path = "/role/saveOrUpdate")
    public R<?> saveOrUpdate(@RequestBody Role role) {
        Role newRole = roleService.insertOrUpdate(role);
        return R.ok("新增或修改成功！", newRole);
    }

    @ApiOperation("根据id查询信息")
    @ApiImplicitParam(name = "id", value = "id")
    @GetMapping("getInfo/{id}")
    @Log(value = "角色-查询单个角色信息", path = "/role/getInfo/{id}")
    public R<?> getInfo(@PathVariable("id") Long id) {
        Role info = roleService.queryById(id);
        return R.ok(info);
    }

    @ApiOperation("根据id删除数据")
    @ApiImplicitParam(name = "ids", value = "id数组")
    @DeleteMapping("delete")
    @Log(value = "角色-删除角色信息", path = "/role/delete")
    public R<?> delete(@RequestParam Long[] ids) {
        roleService.deleteRole(ids);
//        roleService.deleteByIds(ids);
        return R.ok("删除成功！");
    }

    @ApiOperation("根据id查询该角色的权限")
    @ApiImplicitParam(name = "roleId", value = "角色ID")
    @GetMapping("getPermissionsByRoleId/{id}")
    @Log(value = "角色-查询该角色拥有的权限", path = "/role/getPermissionsByRoleId")
    public R<?> getPermissionsByRoleId(@PathVariable("id")  Long id) {
        Map<Integer, String> permissionsByRoleId = roleService.getPermissionsByRoleId(id);
        return R.ok(permissionsByRoleId);
    }


    @ApiOperation("拿到所有角色")
    @GetMapping("getAllRole")
    @Log(value = "角色-查询该角色拥有的权限", path = "/role/getPermissionsByRoleId")
    public R<?> getAllRole(){
        return R.ok(roleService.list());
    }


}



