package com.gusev.spring.core.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class StatisticsAspect{

    private Map<Class<?>, Integer> mapCounter = new HashMap<>();

    public Map<Class<?>, Integer> getMapCounter() {
        return mapCounter;
    }

    @Pointcut("execution(* *.logEvent(..))")
    private void logEventMethods(){
    }


    @AfterReturning("logEventMethods()")
    public void count(JoinPoint joinPoint){
        Class<?> clazz = joinPoint.getTarget().getClass();
        mapCounter.put(clazz, mapCounter.getOrDefault(clazz, 0) + 1);
    }

}
