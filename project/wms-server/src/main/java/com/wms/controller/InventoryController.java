package com.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.aspect.Log;
import com.wms.dto.WarehousingDTO;
import com.wms.pojo.Inventory;
import com.wms.pojo.Task;
import com.wms.service.InventoryService;
import com.wms.utils.PageUtil;
import com.wms.utils.Query;
import com.wms.utils.R;
import com.wms.vo.InventoryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api(tags = "Controller-库存表", value = "/InventoryController")
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Resource
    @Lazy
    private InventoryService inventoryService;

    @ApiOperation("查询数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "库存编码"),
            @ApiImplicitParam(name = "storageId", value = "库位ID")
    })
    @GetMapping("list")
    @Log(value = "库存-查询库存信息",path = "/inventory/list")
    public R<?> list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        IPage<InventoryVo> page = inventoryService.pageList(query.getIPage(InventoryVo.class), query);
        PageUtil pageUtil = new PageUtil(page.getRecords(), page.getTotal(), query.getLimit(), query.getPage());
        return R.ok(pageUtil);
    }

    @ApiOperation("出入库")
    @PostMapping("warehousing")
    @Log(value = "库存-手动出入库",path = "/inventory/warehousing")
    public R<?> saveOrUpdateInventory(@RequestBody List<WarehousingDTO> warehousingDTO){
        return inventoryService.warehousing(warehousingDTO);
    }

    @ApiOperation("智能盘库")
    @GetMapping("intelligentDiskLibrary")
    @Log(value = "库存-智能盘库",path = "/inventory/intelligentDiskLibrary")
    public R<?> intelligentDiskLibrary(){
        inventoryService.intelligentDiskLibrary();
        return R.ok();
    }

    @ApiOperation("获取可用库")
    @GetMapping("billOfInventory")
    @Log(value = "库存-获取可用库",path = "/inventory/billOfInventory")
    public R<?> billOfInventory(){
        return inventoryService.billOfInventory();
    }

    @ApiOperation("库存空间余额")
    @GetMapping("inventoryBalance")
    @Log(value = "库存-各个库存空间余额",path = "/inventory/inventoryBalance")
    public R<?> inventoryBalance(){
        return inventoryService.inventoryBalance();
    }




}
