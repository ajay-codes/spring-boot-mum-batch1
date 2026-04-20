package com.example.aspect;

import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
public class CountMetricAspect {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CountMetricAspect.class);
    private Map<String, Integer> methodExecutionCount= new java.util.concurrent.ConcurrentHashMap<>();

    @Before("execution(* com.example.service.*.*(..))")
    public void countMethodExecution(JoinPoint joinPoint) {
        logger.info("Method executed, counting metric...");
        String methodName = joinPoint.getSignature().toShortString();
        methodExecutionCount.merge(methodName, 1, Integer::sum);
        logger.info("Execution count for {}: {}", methodName, methodExecutionCount.get(methodName));
    }

}
