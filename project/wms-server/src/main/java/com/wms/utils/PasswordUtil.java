package com.wms.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.Random;


public class PasswordUtil {


    private static final Logger log = LoggerFactory.getLogger(PasswordUtil.class);

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) resultSb.append(byteToHexString(value));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            log.info(exception.getMessage());
        }
        return resultString.toUpperCase();
    }

    public static String MD5EncodeUtf8(String origin) {
        return MD5Encode(origin);
    }


    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


    public static String getSalt(){
        String str="zxcvbnmasdfghjklqwertyuiopZXCVBNMASDFGHJKLQWERTYUIOP1234567890,.<>:?";
        Random random = new Random();
        StringBuilder stringBuffer = new StringBuilder();
        //循环16次，共取出16个随机字符
        for (int i = 0; i < 16; i++) {
            //每次生成一个67以内的随机数
            int number = random.nextInt(68);
            //生成的随机数作为 str 字符串的下标；从 str 中取出随机字符后追加到 stringBuffer
            stringBuffer.append(str.charAt(number));
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        String s = MD5EncodeUtf8(MD5EncodeUtf8("adminsalt"));
        String s1 = MD5EncodeUtf8("adminsalt");
        String s2 = MD5EncodeUtf8("admin");
        System.out.println("加密之后：" + s);
        System.out.println("加密之后：" + s1);
        System.out.println("加密之后：" + s2);
        System.out.println(Objects.equals(s, s1));
        String salt = getSalt();
        System.out.println(salt);
    }


}


