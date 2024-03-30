package com.redis04_lock.controller;

import com.redis04_lock.service.InventoryService01;
import com.redis04_lock.service.InventoryService02;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "redis分布式锁测试")
public class InventoryController {

    @Autowired
    private InventoryService02 inventoryService02;

    @GetMapping("/inventory/sale")
    @ApiOperation("扣减库存，一次卖一个")
    public void sale() {
        inventoryService02.sale();
    }
}