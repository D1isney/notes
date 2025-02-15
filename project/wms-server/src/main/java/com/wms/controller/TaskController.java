package com.wms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wms.aspect.Log;
import com.wms.pojo.Task;
import com.wms.service.TaskService;
import com.wms.utils.PageUtil;
import com.wms.utils.Query;
import com.wms.utils.R;
import com.wms.vo.RoleVo;
import com.wms.vo.TaskVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/task")
@Api(tags = "Controller-任务", value = "/task")
public class TaskController {

    @Resource
    private TaskService taskService;

    @ApiOperation("查询数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "任务类型"),
            @ApiImplicitParam(name = "param", value = "任务编码，任务状态"),
            @ApiImplicitParam(name = "limit", value = "分页数量")
    })
    @GetMapping("list")
    @Log(value = "任务-查询所有任务信息", path = "/task/list")
    public R<?> list(@RequestParam Map<String, Object> params){
        Query query = new Query(params);
        IPage<TaskVo> page = taskService.pageList(query.getIPage(TaskVo.class), query);
        PageUtil pageUtil = new PageUtil(page.getRecords(), page.getTotal(), query.getLimit(), query.getPage());
        return R.ok(pageUtil);
    }

    @ApiOperation("更新任务信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "task", value = "任务信息")
    })
    @PostMapping("saveOrUpdateTask")
    @Log(value = "任务-更新任务信息", path = "/task/saveOrUpdateTask")
    public R<?> saveOrUpdateTask(@RequestBody Task task){
        return taskService.saveOrUpdateTask(task);
    }

}
