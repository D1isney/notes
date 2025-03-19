package com.wms.controller;

import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.wms.aspect.Log;
import com.wms.connect.plc.PlcConnect;
import com.wms.constant.SystemParam;
import com.wms.dto.PlcAddressDTO;
import com.wms.service.SystemService;
import com.wms.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 系统配置信息
 */
@RestController
@RequestMapping("/system")
@Api(tags = "Controller-系统", value = "/system")
public class SystemController {

    @Resource
    private SystemService systemService;
    @Resource
    private PlcConnect plcConnect;
    @Resource
    private SystemParam systemParam;

    @ApiOperation("获取PLC配置信息")
    @Log(value = "System-获取PLC系统参数", path = "/system/getSetting")
    @GetMapping("getSetting")
    @PreAuthorize("hasAuthority('/')")
    public R<?> getPlcSetting() {
        return R.ok(systemService.getPlcSetting());
    }

    @ApiOperation("修改保存PLC配置信息")
    @Log(value = "System-修改PLC系统参数", path = "/system/saveOrUpdateSetting")
    @PostMapping("saveOrUpdateSetting")
    @PreAuthorize("hasAuthority('/')")
    public R<?> saveOrUpdateSetting(@RequestBody PlcAddressDTO plcAddressDTO) {
        //  修改参数的时候，把PLC连接给关了
        systemService.saveOrUpdatePlcAddress(plcAddressDTO);
        try {
            plcConnect.close();
            systemParam.setConnect(SystemParam.isConnect_NO);
        } catch (ModbusIOException e) {
            throw new RuntimeException(e);
        }
        return R.ok("修改成功！");
    }

    @ApiOperation("开启PLC连接")
    @GetMapping("open")
    @Log(value = "System-开启PLC连接", path = "/system/open")
    @PreAuthorize("hasAuthority('/')")
    public R<?> openPlcConnect() {
        try {
            plcConnect.open();
            systemParam.setConnect(SystemParam.isConnect_YES);
            //  检查任务
            systemService.rollbackTask();
            return R.ok("PLC连接成功！");
        } catch (ModbusIOException | IOException e) {
            systemParam.setConnect(SystemParam.isConnect_NO);
            return R.error("PLC连接失败，请检查PLC的IP以及端口！");
        }
    }

    @ApiOperation("重新下发任务")
    @GetMapping("executePendingTasks")
    @Log(value = "System-重新下发任务", path = "/system/executePendingTasks")
    @PreAuthorize("hasAuthority('/')")
    public  R<?> executePendingTasks(@RequestParam boolean flag,@RequestParam Integer type) {
        systemService.executePendingTasks(flag,type);
        return R.ok();
    }


    @ApiOperation("关闭PLC连接")
    @GetMapping("close")
    @Log(value = "System-关闭PLC连接", path = "/system/close")
    @PreAuthorize("hasAuthority('/')")
    public R<?> closePlcConnect() {
        try {
            plcConnect.close();
            systemParam.setConnect(SystemParam.isConnect_NO);
            return R.ok("PLC关闭成功！");
        } catch (ModbusIOException e) {
            systemParam.setConnect(SystemParam.isConnect_YES);
            return R.error("PLC关闭失败，请检查PLC的IP以及端口！");
        }
    }

    @ApiOperation("获取PLC状态")
    @GetMapping("plcStatus")
    @Log(value = "System-获取PLC状态", path = "/system/plcStatus")
    @PreAuthorize("hasAuthority('/')")
    public R<?> getSystemPlcStatus() {
        boolean connect = systemParam.isConnect();
        return R.ok(connect);
    }


    @ApiOperation("获取从今天开始算的前7天")
    @GetMapping("getLast7Day")
    @Log(value = "System-获取前七天",path = "/system/getLast7Day")
    @PreAuthorize("hasAuthority('/')")
    public R<?> getLast7Day(){
        return R.ok();
    }


}
