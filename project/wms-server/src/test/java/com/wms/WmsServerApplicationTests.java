package com.wms;

import com.wms.filter.login.PasswordEncoderForSalt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class WmsServerApplicationTests {

//    @Autowired
//    private PasswordEncoderForSalt passwordEncoderForSalt;

    @Test
    void contextLoads() throws ParseException {
//        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
//        String admin = bcryptPasswordEncoder.encode("admin");
//        System.out.println(admin);
//        PasswordEncoderForSalt passwordEncoderForSalt = new PasswordEncoderForSalt();
//        String encode = passwordEncoderForSalt.encode("adminsalt");
//        System.out.println(encode);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse("2019-10-01");
        Date date2 = sdf.parse("2019-10-17");
        int i = date1.compareTo(date2);
        System.out.println(i);
    }

}
