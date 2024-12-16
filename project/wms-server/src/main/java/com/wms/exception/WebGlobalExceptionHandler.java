package com.wms.exception;

import com.wms.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class WebGlobalExceptionHandler {

    @ExceptionHandler(EException.class)
    public R<?> EException(EException e) {
        log.error(e.getMsg());
        return R.error(e.getMsg());
    }
}