package com.gusev.spring.core.aspects;

import com.gusev.spring.core.beans.Event;
import com.gusev.spring.core.loggers.FileEventLogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
