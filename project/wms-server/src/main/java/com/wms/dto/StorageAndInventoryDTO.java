package com.wms.dto;

import com.wms.pojo.Inventory;
import com.wms.pojo.Storage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageAndInventoryDTO {
    private Storage storage;
    private Inventory inventory;
}
