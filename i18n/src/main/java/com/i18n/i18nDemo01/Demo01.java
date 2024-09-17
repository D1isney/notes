package com.i18n.i18nDemo01;

import java.util.Locale;
import java.util.ResourceBundle;

public class Demo01 {
    public static void main(String[] args) {
        //  获取系统默认的语言，国家信息
        Locale locale = Locale.getDefault();
        System.out.println(locale);
        System.out.println();
        for (Locale availableLocale : Locale.getAvailableLocales()) {
            System.out.println(availableLocale);
        }
        testI18n();
    }

    public static void testI18n() {
        //  得到需要的locale对象
        Locale us = Locale.US;
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.message", us);
        String username = bundle.getString("username");
        String password = bundle.getString("password");
        System.out.println(username);
        System.out.println(password);

    }
}
