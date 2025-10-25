package com.demo.user.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.user.aop.annotation.Loggable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final ObjectMapper mapper = new ObjectMapper();

    @Around("@within(com.demo.user.aop.annotation.Loggable) || @annotation(com.demo.user.aop.annotation.Loggable)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String method = signature.getDeclaringTypeName() + "." + signature.getName();
        long start = System.currentTimeMillis();

        Object[] args = pjp.getArgs();
        String argsStr;
        try { argsStr = mapper.writeValueAsString(args); } catch (Exception e) { argsStr = Arrays.toString(args); }

        System.out.printf("[LOG] Enter %s args=%s%n", method, argsStr);
        Object result = pjp.proceed();
        long time = System.currentTimeMillis() - start;
        String resStr;
        try { resStr = mapper.writeValueAsString(result); } catch (Exception e) { resStr = String.valueOf(result); }
        System.out.printf("[LOG] Exit %s time=%dms result=%s%n", method, time, resStr);
        return result;
    }
}
