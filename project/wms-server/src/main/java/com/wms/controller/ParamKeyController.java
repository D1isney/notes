package com.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.aspect.Log;
import com.wms.pojo.ParamKey;
import com.wms.service.ParamKeyService;
import com.wms.utils.PageUtil;
import com.wms.utils.Query;
import com.wms.utils.R;
import com.wms.vo.ParamKeyVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Api(tags = "参数表", value = "/paramKey")
@RestController
@RequestMapping("/paramKey")
public class ParamKeyController {

    @Resource
    private ParamKeyService paramKeyService;

    @ApiOperation("查询数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = " ", value = " ")
    })
    @GetMapping("list")
    @Log(value = "参数-查询所有参数信息", path = "/paramKey/list")
    public R<?> list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        IPage<ParamKeyVo> page = paramKeyService.pageList(query.getIPage(ParamKeyVo.class), query);
        PageUtil pageUtil = new PageUtil(page.getRecords(), page.getTotal(), query.getLimit(), query.getPage());
        return R.ok(pageUtil);
    }


    @ApiOperation("新增或者修改")
    @ApiImplicitParam(name = "ParamKey", value = "paramKey")
    @PostMapping("saveOrUpdate")
    @Log(value = "参数-更改参数信息", path = "/paramKey/saveOrUpdate")
    public R<?> saveOrUpdate(@RequestBody ParamKey paramKey) {
        return paramKeyService.insertOrUpdate(paramKey);
    }

    @ApiOperation("根据id查询信息")
    @ApiImplicitParam(name = "id", value = "id")
    @GetMapping("getInfo/{id}")
    @Log(value = "参数-查询单个参数信息", path = "/paramKey/getInfo/{id}")
    public R<?> getInfo(@PathVariable("id") Long id) {
        ParamKey info = paramKeyService.queryById(id);
        return R.ok(info);
    }

    @ApiOperation("根据id删除数据")
    @ApiImplicitParam(name = "ids", value = "id数组")
    @DeleteMapping("delete")
    @Log(value = "参数-删除参数信息", path = "/paramKey/delete")
    public R<?> delete(@RequestParam Long[] ids) {
        paramKeyService.deleteParamKeyByIds(ids);
        return R.ok("参数删除成功！");
    }


    @ApiOperation("根据类型来查询参数信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "物料类型"),
            @ApiImplicitParam(name = "goodId", value = "物料ID")
    })
    @GetMapping("getParamKeyListByType/{type}/{goodId}")
    @Log(value = "参数-根据Type以及物料ID查询所有的参数", path = "/paramKey/getParamKeyListByType")
    public R<?> getParamKeyListByType(@PathVariable Integer type,@PathVariable Long goodId) {
        return paramKeyService.getParamKeyListByType(type,goodId);
    }


    @ApiOperation("根据类型来查询参数信息")
    @ApiImplicitParam(name = "type", value = "物料类型")
    @GetMapping("getParamKeyListByType/{type}")
    @Log(value = "参数-根据Type查询所有的参数", path = "/paramKey/getParamKeyListByType")
    public R<?> getParamKeyListByType(@PathVariable Integer type) {
        return paramKeyService.getParamKeyListByType(type);
    }

}
