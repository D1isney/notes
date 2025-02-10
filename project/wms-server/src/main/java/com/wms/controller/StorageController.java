package com.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.aspect.Log;
import com.wms.pojo.Storage;
import com.wms.service.StorageService;
import com.wms.utils.PageUtil;
import com.wms.utils.Query;
import com.wms.utils.R;
import com.wms.vo.StorageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/storage")
@Api(tags = "Controller-库位信息", value = "/storage")
public class StorageController {
    @Resource
    private StorageService storageService;

    @ApiOperation("查询数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "库位编码")
    })
    @GetMapping("list")
    @Log(value = "库位-查询库存信息", path = "/storage/list")
    public R<?> list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        IPage<StorageVo> page = storageService.pageList(query.getIPage(StorageVo.class), query);
        PageUtil pageUtil = new PageUtil(page.getRecords(), page.getTotal(), query.getLimit(), query.getPage());
        return R.ok(pageUtil);
    }


    @ApiOperation("保存库位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storage", value = "库位")
    })
    @PostMapping("saveOrUpdate")
    @Log(value = "库位-保存库位信息", path = "/storage/saveOrUpdate")
    public R<?> saveOrUpdate(@RequestBody List<Storage> storage){
        return storageService.saveOrUpdateStorage(storage);
    }


    @ApiOperation("查询库位以及库存信息")
    @GetMapping("queryStorageAndInventory")
    @Log(value = "库位-查询库位以及库存信息", path = "/storage/queryStorageAndInventory")
    public R<?> queryStorageAndInventory(){
        return storageService.queryStorageAndInventory();
    }

    @ApiOperation("查询库位以及库存具体数量以及信息")
    @GetMapping("queryInventoryNum")
    @Log(value = "库位-查询库位以及库存具体数量以及信息", path = "/storage/queryInventoryNum")
    public R<?> queryInventoryNum(){
        return storageService.queryInventoryNum();
    }






}
