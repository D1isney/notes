package com.wms.controller;


import com.wms.aspect.Log;
import com.wms.pojo.Permissions;
import com.wms.utils.PageUtil;
import com.wms.utils.Query;
import com.wms.utils.R;
import com.wms.vo.PermissionsVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import com.wms.service.PermissionsService;

import javax.annotation.Resource;


/**
 * <p>
 *  controller
 * </p>
 */
@Api(tags = "权限信息", value = "/permissions")
@RestController
@RequestMapping("/permissions")
public class PermissionsController {

    @Resource
    private PermissionsService permissionsService;
                                                
    @ApiOperation("查询数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = " ", value = " ")
    })
    @GetMapping("list")
    @Log(value = "权限-查询所有权限信息",path = "/permissions/list")
    public R<?> list(@RequestParam Map<String,Object> params){
        Query query = new Query(params);
        IPage<PermissionsVo> page = permissionsService.pageList(query.getIPage(PermissionsVo.class),query);
        PageUtil pageUtil= new PageUtil(page.getRecords(),page.getTotal(),query.getLimit(),query.getPage());
        return R.ok(pageUtil);
    }


    @ApiOperation("新增或者修改")
    @ApiImplicitParam(name = "Permissions", value = "permissions")
    @PostMapping("saveOrUpdate")
    @Log(value = "权限-更改权限信息",path = "/permissions/saveOrUpdate")
    public R<?> saveOrUpdate(@RequestBody Permissions permissions){
        Permissions newPermissions = permissionsService.insertOrUpdate(permissions);
        return R.ok(newPermissions);
    }

    @ApiOperation("根据id查询信息")
    @ApiImplicitParam(name = "id", value = "id")
    @GetMapping("getInfo/{id}")
    @Log(value = "权限-查询单个权限信息",path = "/permissions/getInfo/{id}")
    public R<?> getInfo(@PathVariable("id")Long id){
        Permissions info = permissionsService.queryById(id);
        return R.ok(info);
    }

    @ApiOperation("根据id删除数据")
    @ApiImplicitParam(name = "ids", value = "id数组")
    @DeleteMapping("delete")
    @Log(value = "权限-删除权限信息",path = "/permissions/delete")
    public R<?> delete(@RequestParam Long[] ids){
        permissionsService.deleteByIds(ids);
        return R.ok();
    }

}



