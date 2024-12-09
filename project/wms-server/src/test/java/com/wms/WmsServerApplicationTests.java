package com.wms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class WmsServerApplicationTests {

    @Test
    void contextLoads() throws ParseException {
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String admin = bcryptPasswordEncoder.encode("admin");
        System.out.println(admin);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse("2019-10-01");
        Date date2 = sdf.parse("2019-10-17");
        int i = date1.compareTo(date2);
        System.out.println(i);
    }

}
