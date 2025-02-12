package com.wms;

import com.wms.filter.login.PasswordEncoderForSalt;
import com.wms.pojo.Inventory;
import com.wms.pojo.Storage;
import com.wms.service.InventoryService;
import com.wms.service.StorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
class WmsServerApplicationTests {

//    @Autowired
//    private PasswordEncoderForSalt passwordEncoderForSalt;

    @Resource
    @Lazy
    private StorageService storageService;
    @Resource
    @Lazy
    private InventoryService inventoryService;

    @Test
    void contextLoads() throws ParseException {
//        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
//        String admin = bcryptPasswordEncoder.encode("admin");
//        System.out.println(admin);
//        PasswordEncoderForSalt passwordEncoderForSalt = new PasswordEncoderForSalt();
//        String encode = passwordEncoderForSalt.encode("adminsalt");
//        System.out.println(encode);


//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date date1 = sdf.parse("2019-10-01");
//        Date date2 = sdf.parse("2019-10-17");
//        int i = date1.compareTo(date2);
//        System.out.println(i);

        List<Storage> storages = storageService.queryAll();
        storages.forEach(storage -> {
            for (int i = 1; i <= 8; i++) {
                Inventory inventory = new Inventory();
                inventory.setLayer(i);
                inventory.setStorageId(storage.getId());
                inventory.setCode(inventoryService.lastCode());
                inventory.setGoodsId(0L);
                inventory.setStatus(0);
                inventory.setName(storage.getName() + "-库存" + i);
                inventory.setCreateTime(new Date());
                inventory.setUpdateTime(new Date());
                inventory.setCreateMember(1L);
                inventory.setUpdateMember(1L);
                inventoryService.saveOrModify(inventory);
            }
        });


    }

}
