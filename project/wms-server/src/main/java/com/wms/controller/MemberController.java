package com.wms.controller;

import com.wms.aspect.Log;
import com.wms.pojo.Member;
import com.wms.service.MemberService;
import com.wms.utils.R;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Disney
 * 用户接口
 */
@RestController
@RequestMapping("/member/")
public class MemberController {

    @Resource
    private MemberService memberService;

    @GetMapping("list")
    @Log(value = "查询所有的用户", path = "/member/list")
    public List<Member> getList() {
        return memberService.list();
    }

    @PostMapping("login")
    @Log(value = "登录接口", path = "/member/login")
    public R<?> login(@RequestBody Member member) {
         return memberService.login(member);
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


}
