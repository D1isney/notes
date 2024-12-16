package com.wms.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PlcAddressDTO {
    private List<AddressValueDTO> pointList;
    private String ip;
    private String port;
    private String slaveId;
}
