package com.wms.aspect;

import com.alibaba.fastjson.JSON;
import com.wms.enums.LogRecordEnum;
import com.wms.pojo.LogRecord;
import com.wms.filter.login.LoginMember;
import com.wms.service.LogRecordService;
import com.wms.thread.MemberThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Date;


@Aspect
@Component
@Slf4j
public class LogAspect {

    @Resource
    private LogRecordService logRecordService;

    @Pointcut("@annotation(com.wms.aspect.Log)")
    public void execute() {
    }

    @Around("execute()")
    Object around(ProceedingJoinPoint pj) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object object = pj.proceed(pj.getArgs());
        Long executeTime = System.currentTimeMillis() - beginTime; // 执行时长(毫秒)
        String params = JSON.toJSONString(pj.getArgs()); // 接口入参
        String result = JSON.toJSONString(object);// 接口返回结果
        MethodSignature methodSignature = (MethodSignature) pj.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Log logInterface = targetMethod.getAnnotation(Log.class); // 从接口注解中获取注解信息
        LoginMember loginMember = MemberThreadLocal.get();
        if (loginMember != null) {
            LogRecord logRecord = getLogRecord(logInterface, params, result, executeTime, loginMember);
            logRecordService.saveOrUpdate(logRecord);
        }
        log.info("有用户调用{}接口", logInterface.path());
        return object;
    }

    public LogRecord getLogRecord(Log log, String params, String result, Long executeTime, LoginMember loginMember) {
        String message = log.value(); // 接口中文名称
        String path = log.path(); // 接口路径
        LogRecord logRecord = new LogRecord();
        logRecord.setMessage(message);
        logRecord.setPath(path);
        logRecord.setParams(params);
        logRecord.setResult(result);
        logRecord.setCreateTime(new Date());
        logRecord.setMemberId(loginMember.getMember().getId() == null ? 0 : loginMember.getMember().getId());
        logRecord.setType(LogRecordEnum.NORMAL_LOG.getCode());
        logRecord.setExecuteTime(executeTime);
        return logRecord;
    }


    @AfterThrowing(value = "execute()", throwing = "runtimeException")
    public void afterThrowingAdvice(JoinPoint pj, RuntimeException runtimeException) {
        System.out.println(pj);
        // 接口调用后异常处理
    }

}