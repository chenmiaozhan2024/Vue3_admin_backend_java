package com.itheima.java_admin_backend.exception;

import com.itheima.java_admin_backend.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常：登录失败、参数校验失败等，直接把错误信息返回给前端
     */
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        log.error("运行时异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 兜底异常：避免把堆栈信息暴露给前端
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常：", e);
        return Result.error("服务器繁忙，请稍后再试");
    }
}
