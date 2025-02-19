package com.wms.dto;

import lombok.*;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TypeAndValue {
    private String text;
    private String value;

    private Long goodId;
    private Long paramId;
    private String name;

    private List<TypeAndValue> list;
}
