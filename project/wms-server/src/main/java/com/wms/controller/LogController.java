package com.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.aspect.Log;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Api(tags = "Controller-日志", value = "/log")
@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogRecordService logService;

    @ApiOperation("查询数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = " ", value = " ")
    })
    @GetMapping("list")
    @PreAuthorize("hasAuthority('/')")
    public R<?> list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        IPage<LogRecordVo> page = logService.pageList(query.getIPage(LogRecordVo.class), query);
        PageUtil pageUtil = new PageUtil(page.getRecords(), page.getTotal(), query.getLimit(), query.getPage());
        return R.ok(pageUtil);
    }


    @ApiOperation("新增或者修改")
    @ApiImplicitParam(name = "Log", value = "log")
    @PostMapping("saveOrUpdate")
    @PreAuthorize("hasAuthority('/')")
    public R<?> saveOrUpdate(@RequestBody LogRecord log) {
        LogRecord newLog = logService.insertOrUpdate(log);
        return R.ok(newLog);
    }

    @ApiOperation("根据id查询信息")
    @ApiImplicitParam(name = "id", value = "id")
    @GetMapping("getInfo/{id}")
    @PreAuthorize("hasAuthority('/')")
    public R<?> getInfo(@PathVariable("id") Long id) {
        LogRecord info = logService.queryById(id);
        return R.ok(info);
    }

    @ApiOperation("根据id删除数据")
    @ApiImplicitParam(name = "ids", value = "id数组")
    @DeleteMapping("delete")
    @Log(value = "日志-删除日志信息",path = "/log/delete")
    @PreAuthorize("hasAuthority('/')")
    public R<?> delete(@RequestParam("ids") Long[] ids) {
        logService.deleteByIds(ids);
        return R.ok();
    }

    @ApiOperation("报警信息统计")
    @GetMapping("alarmStatistics")
    @Log(value = "日志-删除日志信息",path = "/log/alarmStatistics")
    @PreAuthorize("hasAuthority('/')")
    public R<?> alarmStatistics(){
        return logService.alarmStatistics();
    }

}