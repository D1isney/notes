package com.wms.vo;

import com.wms.pojo.Storage;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StorageVo extends Storage {
    private String createUsername;
    private String updateUsername;
}
