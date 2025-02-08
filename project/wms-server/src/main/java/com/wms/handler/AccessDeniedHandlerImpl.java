package com.wms.handler;

import com.alibaba.fastjson.JSON;
import com.wms.exception.EException;
import com.wms.utils.R;
import com.wms.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户权限失败
 *
 * @author Disney
 * @since 2024年5月18日18:05:49
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        R<?> result = new R<>("您的权限不足", HttpStatus.FORBIDDEN.value());
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
    }
}
