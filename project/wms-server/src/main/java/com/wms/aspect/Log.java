package com.wms.aspect;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Component
public @interface Log {
    String value() default ""; // 示例：修改密码

    String path() default ""; // 示例：/sys/user/
}
