package com.space.analysis.aop;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * aop--用于记录日志
 */
@Aspect
@Component
@Log4j2
public class LogAspect {
    @Pointcut("execution( * com.space.analysis.controller.*.*(..))")
    public void methods() {
    }

    /**
     * 方法运行前运行
     */
    @Before("methods()")
    public void doBefore(JoinPoint joinpoint) {
        log.info("=============================Before=================================");

        //接收到请求，记录请求参数
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        log.info("URL:[{}]", request.getRequestURL().toString());
        log.info("HTTP_METHOD:[{}]", request.getMethod());
        log.info("IP:[{}]", request.getRemoteAddr());
        log.info("CLASS_METHOD:[{}]", joinpoint.getSignature().getDeclaringTypeName() + "." + joinpoint.getSignature().getName());
        log.info("ARGS:\r\n{}", JSONUtil.toJsonStr(joinpoint.getArgs()));

    }


    /**
     * 环绕通知
     */
    @Around("methods()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        long start = System.currentTimeMillis();

        Object[] args = joinPoint.getArgs();
        log.info("=============================环绕通知开始=================================");

        Object result;
        try {
            result = joinPoint.proceed(args);

        } catch (Throwable e) {
            log.info("=============================切面捕获异常=================================");
            JSONObject response = new JSONObject();
            response.putOpt("returnCode", "999999");
            response.putOpt("returnMsg", "失败");
            response.putOpt("timestamp", DateUtil.current());
            result = response;
        }

        log.info("环绕通知结束:\r\n{}", JSONUtil.toJsonStr(result));
        long end = System.currentTimeMillis();
        log.info("=============================耗时:[{}]ms=================================", end - start);

        return result;

    }


}
