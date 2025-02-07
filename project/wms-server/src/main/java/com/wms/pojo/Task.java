package com.wms.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString
@TableName(value = "task")
@ApiModel(value = "Task")
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增主键")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "物料id")
    @TableField(value = "`goods_id`")
    private Long goodsId;

    @ApiModelProperty(value = "库存id")
    @TableField(value = "`inventory_id`")
    private Long inventoryId;

    @ApiModelProperty(value = "任务编码")
    @TableField(value = "`code`")
    private String code;

    @ApiModelProperty(value = "任务状态（0：初始化，1：进行中，2：挂起，3：完成）")
    @TableField(value = "`status`")
    private Integer status;

    @ApiModelProperty(value = "任务类型（0：入库，1：出库）")
    @TableField(value = "`type`")
    private Integer type;

    @ApiModelProperty(value = "创建时间")
    @TableField("`create_time`")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("`update_time`")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    @TableField("`create_member`")
    private Long createMember;

    @ApiModelProperty(value = "更新人")
    @TableField("`update_member`")
    private Long updateMember;

    @ApiModelProperty(value = "备注")
    @TableField("`remark`")
    private String remark;
}
