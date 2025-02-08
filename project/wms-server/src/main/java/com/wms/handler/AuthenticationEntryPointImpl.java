package com.wms.handler;

import com.alibaba.fastjson.JSON;

import com.wms.exception.EException;
import com.wms.utils.R;
import com.wms.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 用户认证失败
 *
 * @author Disney
 * @since 2024年5月18日18:05:40
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    //  异常处理
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        R<?> result = new R<>(authException.getMessage(), HttpStatus.UNAUTHORIZED.value());
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response, json);
    }
}
