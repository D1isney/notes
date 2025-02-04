package com.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.aspect.Log;
import com.wms.dto.TypeAndValue;
import com.wms.pojo.Goods;
import com.wms.service.GoodsService;
import com.wms.utils.PageUtil;
import com.wms.utils.Query;
import com.wms.utils.R;
import com.wms.vo.GoodsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api(tags = "物料表", value = "/goods")
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    @ApiOperation("查询数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "物料类型"),
            @ApiImplicitParam(name = "code", value = "物料编码"),
            @ApiImplicitParam(name = "name", value = "物料名称")
    })
    @GetMapping("list")
    @Log(value = "物料-查询所有物料信息", path = "/goods/list")
    public R<?> list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        IPage<GoodsVo> page = goodsService.pageList(query.getIPage(GoodsVo.class), query);
        PageUtil pageUtil = new PageUtil(page.getRecords(), page.getTotal(), query.getLimit(), query.getPage());
        return R.ok(pageUtil);
    }


    @ApiOperation("通过Id查询物料的所有参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goodId", value = "物料ID")
    })
    @GetMapping("getGoodsParamByGoodId/{goodId}")
    @Log(value = "物料-通过物料ID查询所有的物料参数",path = "/goods/getGoodsParamByGoodId")
    public R<?> getGoodsParamByGoodId(@PathVariable Long goodId) {
        List<TypeAndValue> typeAndValue = goodsService.getTypeAndValue(goodId);
        return R.ok(typeAndValue);
    }


    @ApiOperation("通过Id查询物料的所有参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "goods", value = "物料所有信息")
    })
    @GetMapping("saveOrUpdateGoods}")
    @Log(value = "物料-修改物料信息",path = "/goods/saveOrUpdateGoods")
    public R<?> saveOrUpdateGoods(@RequestBody Goods goods){
        return goodsService.saveOrUpdateGoods(goods);
    }




}
