package com.wms.runner;

import com.wms.connect.plc.PlcConnect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.wms.enums.PLCEnum.*;


@Component
@Slf4j
public class WmsApplicationRunner implements ApplicationRunner {

    @Resource
    private PlcConnect plcConnect;





    @Override
    public void run(ApplicationArguments args) {
//        try {
//            plcConnect.open();
//            System.out.println(plcConnect.readPlc(0));
//            System.out.println(plcConnect.readPlc(1));
//            System.out.println(plcConnect.readPlc(2));
//            plcConnect.writePlc(0,1);
//            plcConnect.writePlc(1,2);
//            plcConnect.writePlc(2,3);
//            System.out.println(plcConnect.readPlc(0));
//            System.out.println(plcConnect.readPlc(1));
//            System.out.println(plcConnect.readPlc(2));
//
//            System.out.println();
//            System.out.println(plcConnect.readPlc(PLC_INT));
//            System.out.println(plcConnect.readPlc(PLC_RFID_IN));
//            System.out.println(plcConnect.readPlc(PLC_STORAGE_LEVEL_IN));
//            plcConnect.writePlc(PLC_INT,10);
//            plcConnect.writePlc(PLC_RFID_IN,20);
//            plcConnect.writePlc(PLC_STORAGE_LEVEL_IN,30);
//            System.out.println(plcConnect.readPlc(PLC_INT));
//            System.out.println(plcConnect.readPlc(PLC_RFID_IN));
//            System.out.println(plcConnect.readPlc(PLC_STORAGE_LEVEL_IN));
//            plcConnect.close();
//        }catch (Exception e) {
//            e.printStackTrace();
//        }



    }


}
