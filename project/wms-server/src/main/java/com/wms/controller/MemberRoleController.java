package com.wms.controller;


import com.wms.aspect.Log;
import com.wms.pojo.MemberRole;
import com.wms.utils.PageUtil;
import com.wms.utils.Query;
import com.wms.utils.R;
import com.wms.vo.MemberRoleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import com.wms.service.MemberRoleService;

import javax.annotation.Resource;


/**
 * <p>
 *  controller
 * </p>
 */
@Api(tags = "Controller-用户-角色", value = "/memberRole")
@RestController
@RequestMapping("/memberRole")
public class MemberRoleController {

    @Resource
    private MemberRoleService memberRoleService;
                                                
    @ApiOperation("查询数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = " ", value = " ")
    })
    @GetMapping("list")
    @Log(value = "用户-角色-查询所有用户角色信息",path = "/memberRole/list")
    @PreAuthorize("hasAuthority('memberRole:list')")
    public R<?> list(@RequestParam Map<String,Object> params){
        Query query = new Query(params);
        IPage<MemberRoleVo> page = memberRoleService.pageList(query.getIPage(MemberRoleVo.class),query);
        PageUtil pageUtil= new PageUtil(page.getRecords(),page.getTotal(),query.getLimit(),query.getPage());
        return R.ok(pageUtil);
    }


    @ApiOperation("新增或者修改")
    @ApiImplicitParam(name = "MemberRole", value = "memberRole")
    @PostMapping("saveOrUpdate")
    @Log(value = "用户-角色-保存用户角色信息",path = "/memberRole/saveOrUpdate")
    @PreAuthorize("hasAuthority('memberRole:saveOrUpdate')")
    public R<?> saveOrUpdate(@RequestBody MemberRole memberRole){
        MemberRole newMemberRole = memberRoleService.insertOrUpdate(memberRole);
        return R.ok(newMemberRole);
    }

    @ApiOperation("根据id查询信息")
    @ApiImplicitParam(name = "id", value = "id")
    @GetMapping("getInfo/{id}")
    @Log(value = "用户-角色-查询单个用户角色信息",path = "/memberRole/getInfo/{id}")
    @PreAuthorize("hasAuthority('memberRole:getInfo')")
    public R<?> getInfo(@PathVariable("id")Long id){
        MemberRole info = memberRoleService.queryById(id);
        return R.ok(info);
    }

    @ApiOperation("根据id删除数据")
    @ApiImplicitParam(name = "ids", value = "id数组")
    @DeleteMapping("delete")
    @Log(value = "用户-角色-删除用户角色信息",path = "/memberRole/delete")
    @PreAuthorize("hasAuthority('memberRole:delete')")
    public R<?> delete(@RequestParam Long[] ids){
        memberRoleService.deleteByIds(ids);
        return R.ok();
    }

}



