package com.wms.controller;

import com.wms.aspect.Log;
import com.wms.filter.login.Member;
import com.wms.service.MemberService;
import com.wms.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Disney
 * 用户接口
 */
@RestController
@RequestMapping("/member")
@Api(tags = "用户", value = "/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @GetMapping("list")
    @Log(value = "查询所有的用户", path = "/member/list")
    @PreAuthorize("hasAuthority('/member/list')")
    public List<Member> getList() {
        return memberService.list();
    }

    @Log(value = "登录接口", path = "/member/login")
    @PostMapping("login")
    public R<?> login(@RequestBody Member member) {
        return memberService.login(member);
    }



    @PostMapping("constraintLogin")
    @Log(value = "强制登录",path = "/member/constraintLogin")
    public R<?> constraintLogin(@RequestBody Member member){
        return memberService.constraintLogin(member);
    }

    @GetMapping("logout")
    @Log(value = "登出接口", path = "/member/logout")
    public R<?> logoUt() {
        memberService.logout();
        return R.ok("登出成功！");
    }

    @PostMapping("register")
    @Log(value = "注册接口", path = "/member/register")
    public R<?> add(@RequestBody Member member) {
        boolean b = memberService.saveMemberDetails(member);
        if (b) {
            return R.ok("添加成功");
        } else {
            return R.error("添加失败");
        }
    }

    @PostMapping("saveOrUpdate")
    @Log(value = "保存或修改接口", path = "/member/saveOrUpdate")
    @PreAuthorize("hasAuthority('/member/saveOrUpdate')")
    public R<?> saveOrUpdate(@RequestBody Member member) {
        boolean b = memberService.insertOrSave(member);
        if (b) {
            return R.ok();
        }else{
            return R.error("保存或插入失败！");
        }
    }

    @ApiOperation("根据id删除数据")
    @ApiImplicitParam(name = "ids", value = "id数组")
    @DeleteMapping("delete")
    @Log(value = "用户-删除用户信息",path = "/member/delete")
    @PreAuthorize("hasAuthority('/member/delete')")
    public R<?> delete(@RequestParam Long[] ids){
        memberService.deleteByIds(ids);
        return R.ok();
    }




}
