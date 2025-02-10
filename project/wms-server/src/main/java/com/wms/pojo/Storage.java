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
import java.util.List;

@Setter
@Getter
@ToString
@TableName(value = "storage")
@ApiModel(value = "Storage")
public class Storage implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增主键")
    @TableId(value = "`id`", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "库位编码")
    @TableField("`code`")
    private String code;

    @ApiModelProperty(value = "是否禁用（true：可以使用，false: 不可以使用）")
    @TableField("`is_forbidden`")
    private boolean isForbidden;

    @ApiModelProperty(value = "库位名称")
    @TableField("`name`")
    private String name;

    @ApiModelProperty(value = "库位名称")
    @TableField("`row`")
    private Integer row;

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

    @ApiModelProperty(value = "具体库位的层")
    @TableField(exist = false)
    private List<Inventory> inventoryList;

}
