package com.wms.controller;


import com.wms.aspect.Log;
import com.wms.pojo.RolePermissions;
import com.wms.utils.PageUtil;
import com.wms.utils.Query;
import com.wms.utils.R;
import com.wms.vo.RolePermissionsVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import com.wms.service.RolePermissionsService;

import javax.annotation.Resource;


/**
 * <p>
 *  controller
 * </p>
 */
@Api(tags = "角色-权限", value = "/rolePermissions")
@RestController
@RequestMapping("/rolePermissions")
public class RolePermissionsController {

    @Resource
    private RolePermissionsService rolePermissionsService;
                                                
    @ApiOperation("查询数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = " ", value = " ")
    })
    @GetMapping("list")
    @Log(value = "角色-权限-查询所有角色权限信息",path = "/rolePermissions/list")
    public R<?> list(@RequestParam Map<String,Object> params){
        Query query = new Query(params);
        IPage<RolePermissionsVo> page = rolePermissionsService.pageList(query.getIPage(RolePermissionsVo.class),query);
        PageUtil pageUtil= new PageUtil(page.getRecords(),page.getTotal(),query.getLimit(),query.getPage());
        return R.ok(pageUtil);
    }


    @ApiOperation("新增或者修改")
    @ApiImplicitParam(name = "RolePermissions", value = "rolePermissions")
    @PostMapping("saveOrUpdate")
    @Log(value = "角色-权限-保存角色权限信息",path = "/rolePermissions/saveOrUpdate")
    public R<?> saveOrUpdate(@RequestBody RolePermissions rolePermissions){
        RolePermissions newRolePermissions = rolePermissionsService.insertOrUpdate(rolePermissions);
        return R.ok(newRolePermissions);
    }

    @ApiOperation("根据id查询信息")
    @ApiImplicitParam(name = "id", value = "id")
    @GetMapping("getInfo/{id}")
    @Log(value = "角色-权限-查询单个角色权限信息",path = "/rolePermissions/getInfo/{id}")
    public R<?> getInfo(@PathVariable("id")Long id){
        RolePermissions info = rolePermissionsService.queryById(id);
        return R.ok(info);
    }

    @ApiOperation("根据id删除数据")
    @ApiImplicitParam(name = "ids", value = "id数组")
    @DeleteMapping("delete")
    @Log(value = "角色-权限-删除角色权限信息",path = "/rolePermissions/delete")
    public R<?> delete(@RequestParam Long[] ids){
        rolePermissionsService.deleteByIds(ids);
        return R.ok();
    }

}



