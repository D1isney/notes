package com.wms.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AddressValueDTO {
    private Integer address;
    private String name;
    private Integer value;
}
