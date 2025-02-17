package com.wms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentSelector {
    private String value;
    private String label;
    //  是否可用
    private boolean disabled;
    private List<ChildrenSelector> children;
}
