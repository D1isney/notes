package com.wms.dto;

import lombok.*;

/**
 * 返回给前端的角色以及角色ID
 */
@Data
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

    //  角色名称
    private String label;
    //  角色ID
    private Long value;

}
