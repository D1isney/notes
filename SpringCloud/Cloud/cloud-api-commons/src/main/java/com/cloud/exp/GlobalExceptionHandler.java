package com.cloud.exp;

import com.cloud.resp.ResultData;
import com.cloud.resp.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
/*
 * 自定义客户端返回格式
 * 捕获客户端返回异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    //  500服务器内部错误
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResultData<String> exception(Exception e) {
        System.out.println("### come in GlobalExceptionHandler");
        log.error("全局异常信息：{}", e.getMessage(), e);
        return ResultData.fail(ReturnCodeEnum.RC500.getCode(), e.getMessage());
    }
}
