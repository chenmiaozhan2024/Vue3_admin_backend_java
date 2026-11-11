package com.itheima.java_admin_backend.exception;

import com.itheima.java_admin_backend.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 静态资源（图片等）不存在：属于正常的 404，不需要打 ERROR 堆栈刷屏
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result handleNoResourceFound(NoResourceFoundException e) {
        log.warn("静态资源不存在：{}", e.getMessage());
        return Result.error("资源不存在");
    }

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
