package com.gusev.spring.core.aspects;

import com.gusev.spring.core.beans.Event;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class StatisticsAspect{


    private int maxId = 0;

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

    @Around("logEventMethods() && args(event)")
    public void saveMaxEventId(ProceedingJoinPoint joinPoint, Event event) throws Throwable {
        if(maxId < event.getId()) maxId = event.getId();
        joinPoint.proceed(new Object[]{event});
    }

    public int getMaxId() {
        return maxId;
    }
}
