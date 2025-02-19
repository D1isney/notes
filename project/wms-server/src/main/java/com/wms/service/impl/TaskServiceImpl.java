package com.wms.service.impl;

import com.wms.dao.TaskDao;
import com.wms.dto.GoodsCodeAndInventoryCodeDTO;
import com.wms.dto.TaskDataDTO;
import com.wms.dto.TypeAndValue;
import com.wms.dto.WarehousingDTO;
import com.wms.enums.TaskEnum;
import com.wms.exception.EException;
import com.wms.pojo.Goods;
import com.wms.pojo.Inventory;
import com.wms.pojo.Task;
import com.wms.service.GoodsService;
import com.wms.service.InventoryService;
import com.wms.service.TaskService;
import com.wms.service.base.IBaseServiceImpl;
import com.wms.thread.MemberThreadLocal;
import com.wms.utils.CodeUtils;
import com.wms.utils.DateUtil;
import com.wms.utils.R;
import com.wms.utils.StringUtil;
import com.wms.vo.TaskVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TaskServiceImpl extends IBaseServiceImpl<TaskDao, Task, TaskVo> implements TaskService {
    @Resource
    private TaskDao taskDao;

    @Resource
    @Lazy
    private InventoryService inventoryService;

    @Resource
    @Lazy
    private GoodsService goodsService;

    @Override
    public String lastCode() {
        return CodeUtils.getString((taskDao.lastCode()));
    }

    /**
     * 更新任务信息、判断任务是否直接下发
     *
     * @param task 任务
     * @return R
     */
    @Override
    public R<?> saveOrUpdateTask(Task task) {
        //  保存新的任务
        if (StringUtil.isEmpty(task.getId())) {
            return createTask(task);
        } else {
            //  修改任务信息
            return updateTask(task);
        }
    }

    /**
     * 手动下发任务。
     *
     * @param task 需要操作的任务
     * @return R
     */
    @Override
    public R<?> manualOperationIssued(Task task) {
        if (StringUtil.isEmpty(task.getId())) {
            throw new EException("任务ID不能为空！！");
        } else {
            //  找到对应的物料，库存
            if (StringUtil.isEmpty(task.getGoodsId())) {
                throw new EException("请先保存需要操作的物料！！");
            }
            if (StringUtil.isEmpty(task.getInventoryId())) {
                throw new EException("请先保存需要操作的库位！！");
            }
            Goods goodsById = goodsService.getGoodsById(task.getGoodsId());
            Inventory inventoryById = inventoryService.queryById(task.getInventoryId());
            TaskDataDTO taskDataDTO = new TaskDataDTO();
            taskDataDTO.setGoodsCode(goodsById.getCode());
            taskDataDTO.setInventoryCode(inventoryById.getCode());
            //  下发
            return issued(taskDataDTO, task);
        }
    }

    /**
     * 通过任务找到对应的物料以及库存Code
     *
     * @param task 任务
     * @return R
     */
    @Override
    public R<?> getGoodsAndInventory(Task task) {
        if (StringUtil.isEmpty(task.getId())) {
            throw new EException("任务ID不能为空！！");
        } else {
            GoodsCodeAndInventoryCodeDTO dto = new GoodsCodeAndInventoryCodeDTO();
            if (StringUtil.isEmpty(task.getGoodsId())) {
                dto.setGoodsCode(null);
            } else {
                Goods goodsById = goodsService.getGoodsById(task.getGoodsId());
                if (StringUtil.isEmpty(goodsById)) {
                    dto.setGoodsCode(null);
                } else {
                    dto.setGoodsCode(goodsById.getCode());
                }
            }
            if (StringUtil.isEmpty(task.getInventoryId())) {
                dto.setInventoryCode(null);
            } else {
                Inventory inventoryById = inventoryService.queryById(task.getInventoryId());
                if (StringUtil.isEmpty(inventoryById)) {
                    dto.setInventoryCode(null);
                } else {
                    dto.setInventoryCode(inventoryById.getCode());
                }
            }
            return R.ok(dto);
        }
    }

    @Override
    public R<?> deleteTask(Long[] ids) {
        Arrays.stream(ids).forEach(id -> {
            Task task = queryById(id);
            if (!TaskEnum.INIT_IN.getStatus().equals(task.getStatus()) && !TaskEnum.ACCOMPLISH_IN.getStatus().equals(task.getStatus())) {
                throw new EException("删除失败，任务：" + task.getCode() + "，已下发，不能删除该任务！");
            }
        });
        deleteByIds(ids);
        return R.ok("删除成功！");

    }


    @Value("${data.queryDays}")
    private Integer queryDays;

    /**
     * 一周每天的任务量
     *
     * @return R
     */
    @Override
    public R<?> weeklyWorkload() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<TypeAndValue> list = new ArrayList<>();
        for (int i = -queryDays; i <= 0; i++) {
            Date date = DateUtil.currentAdd(i);
            date = DateUtil.resetTimeToStartOfDay(date);
            Map<String, Object> map = new HashMap<>();
            map.put("createTime", date);
            List<Task> tasks = queryList(map);
            TypeAndValue typeAndValue = new TypeAndValue();

            typeAndValue.setName(sdf.format(date));
            typeAndValue.setValue(String.valueOf(tasks.size()));
            list.add(typeAndValue);
        }
        return R.ok(list);
    }

    @Override
    public R<?> inboundAndOutboundVolume() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,List<TypeAndValue>> mapList = new HashMap<>();

        //  出库量
        List<TypeAndValue> out = new ArrayList<>();
        for (int i = -queryDays; i <= 0; i++) {
            Date date = DateUtil.currentAdd(i);
            date = DateUtil.resetTimeToStartOfDay(date);
            Map<String, Object> map = new HashMap<>();
            map.put("createTime", date);
            map.put("type", TaskEnum.INIT_OUT.getType());
            List<Task> tasks = queryList(map);
            TypeAndValue typeAndValue = new TypeAndValue();
            typeAndValue.setName(sdf.format(date));
            typeAndValue.setValue(String.valueOf(tasks.size()));
            out.add(typeAndValue);
        }

        List<TypeAndValue> in = new ArrayList<>();
        for (int i = -queryDays; i <= 0; i++) {
            Date date = DateUtil.currentAdd(i);
            date = DateUtil.resetTimeToStartOfDay(date);
            Map<String, Object> map = new HashMap<>();
            map.put("createTime", date);
            map.put("type", TaskEnum.INIT_IN.getType());
            List<Task> tasks = queryList(map);
            TypeAndValue typeAndValue = new TypeAndValue();
            typeAndValue.setName(sdf.format(date));
            typeAndValue.setValue(String.valueOf(tasks.size()));
            in.add(typeAndValue);
        }

        mapList.put("in",in);
        mapList.put("out",out);
        return R.ok(mapList);
    }


    /**
     * 修改任务
     *
     * @param task 任务
     */
    public R<?> updateTask(Task task) {
        if (!TaskEnum.INIT_IN.getStatus().equals(task.getStatus())) {
            throw new EException("该任务已下发或已挂起，无法修改任务信息！");
        }
        task = verificationParameter(task);
        task.setUpdateMember(getCurrentMember());
        task.setUpdateTime(new Date());
        saveOrModify(task);
        //  是否直接下发
        if (task.isDirectlyIssued()) {
            return issued(task.getTaskDataDTOS(), task);
        }
        return R.ok("修改成功！");
    }

    /**
     * 下发
     *
     * @param taskDataDTOS 参数
     * @param task         具体的任务
     */
    public R<?> issued(TaskDataDTO taskDataDTOS, Task task) {
        if (StringUtil.isEmpty(taskDataDTOS)) {
            throw new EException("对应的物料以及库存不能为空！！！");
        }
        if (StringUtil.isEmpty(taskDataDTOS.getGoodsCode())) {
            throw new EException("对应的物料不能为空！！！");
        }
        if (StringUtil.isEmpty(taskDataDTOS.getInventoryCode())) {
            throw new EException("对应的库存不能为空！！！");
        }
        //  直接下发
        List<WarehousingDTO> warehousingDTO = new ArrayList<>();
        WarehousingDTO warehousing = new WarehousingDTO();
        warehousing.setGoodsCode(taskDataDTOS.getGoodsCode());
        warehousing.setInventoryCode(taskDataDTOS.getInventoryCode());
        warehousing.setType(task.getType() - 4); // （4 ~ 5） - 4 = （ 0 ~ 1 ）
        warehousing.setStorageCode(null);
        warehousing.setTask(task);
        warehousingDTO.add(warehousing);
        return inventoryService.operatingDuty(warehousingDTO);
    }

    /**
     * 查询物料以及库存ID
     *
     * @param task 任务
     * @return 任务
     */
    public Task verificationParameter(Task task) {
        TaskDataDTO taskDataDTOS = task.getTaskDataDTOS();
        if (!StringUtil.isEmpty(taskDataDTOS)) {
            if (!StringUtil.isEmpty(taskDataDTOS.getGoodsCode())) {
                Goods goodsByCode = goodsService.getGoodsByCode(taskDataDTOS.getGoodsCode());
                task.setGoodsId(goodsByCode.getId());
            }
            if (!StringUtil.isEmpty(taskDataDTOS.getInventoryCode())) {
                if (TaskEnum.INIT_IN.getType().equals(task.getType())) {
                    //  入库任务
                    Inventory inventoryByCode = inventoryService.getInventoryByCode(taskDataDTOS.getInventoryCode(), TaskEnum.INIT_IN.getType(), null);
                    task.setInventoryId(inventoryByCode.getId());
                } else if (TaskEnum.INIT_OUT.getType().equals(task.getType())) {
                    //  出库任务
                    Inventory inventoryByCode = inventoryService.getInventoryByCode(taskDataDTOS.getInventoryCode(), TaskEnum.INIT_OUT.getType(), null);
                    task.setInventoryId(inventoryByCode.getId());
                }
            }
        }
        return task;
    }


    /**
     * 创建任务
     *
     * @param task 任务
     */
    public R<?> createTask(Task task) {
        task = verificationParameter(task);
        task.setCreateMember(getCurrentMember());
        task.setUpdateMember(getCurrentMember());
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        task.setStatus(TaskEnum.INIT_IN.getStatus());
        task.setCode(lastCode());
        saveOrModify(task);
        //  是否直接下发
        if (task.isDirectlyIssued()) {
            return issued(task.getTaskDataDTOS(), task);
        }
        return R.ok("保存成功！");
    }

    public Long getCurrentMember() {
        return MemberThreadLocal.get().getMember().getId();
    }
}
