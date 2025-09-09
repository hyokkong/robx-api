package com.rbox.common.logging;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Simple AOP based logger that traces entry, exit and exceptions
 * for controllers, services and repositories.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAround(joinPoint, "Controller");
    }

    @Around("within(@org.springframework.stereotype.Service *) || within(@org.springframework.stereotype.Repository *)")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAround(joinPoint, "Service");
    }

    private Object logAround(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("[{}] Enter {} with args {}", layer, methodName, Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.info("[{}] Exit {} with result {}", layer, methodName, result);
            return result;
        } catch (Throwable e) {
            log.error("[{}] Exception in {}", layer, methodName, e);
            throw e;
        }
    }
}
