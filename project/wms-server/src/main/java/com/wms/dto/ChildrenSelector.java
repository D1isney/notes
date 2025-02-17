package com.wms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildrenSelector {
    //  value就是code
    private String value;
    //  label就是name
    private String label;
    //  是否可用
    private boolean disabled;
}
