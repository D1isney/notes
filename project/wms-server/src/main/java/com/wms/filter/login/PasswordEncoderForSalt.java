package com.wms.filter.login;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.wms.utils.PasswordUtil.MD5EncodeUtf8;

// 加密方式
//@Component
public class PasswordEncoderForSalt implements PasswordEncoder {

    /**
     * 双重MD5加密
     * @param rawPassword 密码
     * @return 加密后的密码
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return MD5EncodeUtf8(MD5EncodeUtf8((String) rawPassword));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
//        String encode = encode(rawPassword);
        return Objects.equals(rawPassword, encodedPassword);
    }
}
