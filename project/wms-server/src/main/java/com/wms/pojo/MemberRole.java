package com.wms.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户-角色表
 * @author Disney
 * @since 2024年12月10日23:12:52
 */
@TableName("member_role")
@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MemberRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO,value = "id")
    private Long id;
    @TableField(value = "member_id")
    private Long memberId;
    @TableField(value = "role_id")
    private Long roleId;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "create_member")
    private Long createMember;
    @TableField(value = "update_member")
    private Long updateMember;

    @TableField(value = "remark")
    private String remark;

}
