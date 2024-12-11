package com.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.pojo.LogRecord;
import com.wms.service.LogRecordService;
import com.wms.utils.PageUtil;
import com.wms.utils.Query;
import com.wms.utils.R;
import com.wms.vo.LogRecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "", value = "/log")
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogRecordService logService;

    @ApiOperation("查询数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "1", value = "1")
    })
    @GetMapping("list")
    public R<?> list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        IPage<LogRecordVo> page = logService.pageList(query.getIPage(LogRecordVo.class), query);
        PageUtil pageUtil = new PageUtil(page.getRecords(), page.getTotal(), query.getLimit(), query.getPage());
        return R.ok(pageUtil);
    }


    @ApiOperation("新增或者修改")
    @ApiImplicitParam(name = "Log", value = "log")
    @PostMapping("saveOrUpdate")
    public R<?> saveOrUpdate(@RequestBody LogRecord log) {
        LogRecord newLog = logService.insertOrUpdate(log);
        return R.ok(newLog);
    }

    @ApiOperation("根据id查询信息")
    @ApiImplicitParam(name = "id", value = "id")
    @GetMapping("getInfo/{id}")
    public R<?> getInfo(@PathVariable("id") Long id) {
        LogRecord info = logService.queryById(id);
        return R.ok(info);
    }

    @ApiOperation("根据id删除数据")
    @ApiImplicitParam(name = "ids", value = "id数组")
    @DeleteMapping("delete")
    public R<?> delete(@RequestParam("ids") Long[] ids) {
        logService.deleteByIds(ids);
        return R.ok();
    }

}