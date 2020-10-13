package com.piemon.gmall.admin.aop;

import com.piemon.gmall.to.CommonResult;
import com.sun.corba.se.spi.ior.ObjectKey;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/**
 * @author: piemon
 * @date: 2020/10/10 23:45
 * @description:
 */
@Slf4j
@Aspect
@Component
public class DataVaildAspect {
    @Around("execution(* com.piemon.gmall.admin..*Controller.*(..))")
    public Object validAround(ProceedingJoinPoint point){

        Object proceed = null;
        try {

            Object[] args = point.getArgs();
            for(Object obj:args){
                if(obj instanceof BindingResult){
                    BindingResult r = (BindingResult) obj;
                    if(r.getErrorCount()>0){
                        //框架自动校验检测出错误
                        return new CommonResult().validateFailed(r);
                    }
                }
            }
            log.debug("校验切面介入工作......");
            //System.out.println("前置通知");

            //这就是反射的  method.invoke();
            proceed = point.proceed(point.getArgs());
            //System.out.println("返回通知");
            log.debug("校验切面将目标方法已经放行...{}",proceed);
        } catch (Throwable throwable) {
            //System.out.println("异常通知");
            throw new RuntimeException(throwable);
        }finally {
            //System.out.println("后置通知");
        }
        return proceed;
    }
}
