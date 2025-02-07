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
@TableName(value = "inventory")
@ApiModel(value = "Inventory")
public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增主键")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "库位ID")
    @TableField("`storage_id`")
    private Long storageId;

    @ApiModelProperty(value = "物料ID")
    @TableField("`goods_id`")
    private Long goodsId;

    @ApiModelProperty(value = "库存名称")
    @TableField("`name`")
    private String name;

    @ApiModelProperty(value = "库存编码")
    @TableField("`code`")
    private String code;

    @ApiModelProperty(value = "库存层数")
    @TableField("`layer`")
    private int layer;

    @ApiModelProperty(value = "库存状态（0：无货物，1：正在入库，2：入库完成，3：正在出库，4：出库完成，5：有货物）")
    private Integer status;

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

    @ApiModelProperty(value = "描述")
    @TableField("`remark`")
    private String remark;

}
