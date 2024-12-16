package com.wms.runner;

import com.wms.constant.ConfigConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
@Slf4j
public class WmsApplicationRunner implements ApplicationRunner {

    @Value("${plc.address}")
    private String plcAddress;

    public String PLC_ADDRESS = "";
    @PostConstruct
    public void init(){
        setPlcAddress(plcAddress);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("-------------------------------------------");
        log.info("PLC Address: {}", PLC_ADDRESS);
        System.out.println("-------------------------------------------");



    }

    public void setPlcAddress(String plcAddress) {
        this.PLC_ADDRESS = ConfigConstant.CONF_BASE_MODBUS + plcAddress;
    }


}
