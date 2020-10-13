package com.piemon.gmall.admin.aop;

import com.piemon.gmall.to.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: piemon
 * @date: 2020/10/11 21:26
 * @description:统一处理所有异常，给前端返回500的json
 * 当我们编写了环绕通知时，注意将异常抛出
 */
@Slf4j
@RestControllerAdvice
//RestControllerAdvice=ReponseBody(返回json)+ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {ArithmeticException.class})
    public Object handlerException(Exception exception){

        log.error("系统全局异常感知，信息:{}",exception.getStackTrace());

        return new CommonResult().validateFailed("数学错误");
    }

    @ResponseBody
    @ExceptionHandler(value = {NullPointerException.class})
    public Object handlerException02(Exception exception){
        log.error("系统出现异常感知，信息:{}",exception.getMessage());
        return new CommonResult().validateFailed("空指针");
    }
}
