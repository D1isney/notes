package com.wms.controller;

import com.wms.exception.EException;
import com.wms.pojo.Role;
import com.wms.service.RolePermissionsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/exception")
public class ExceptionTest {

    @Resource
    private RolePermissionsService permissionsService;

    @GetMapping("/test")
    public void test() {
        throw new EException("测试异常");
    }

    @GetMapping("/test1")
    public void test1(){
        permissionsService.createDefaultRolePermissions(new Role());
    }
}
