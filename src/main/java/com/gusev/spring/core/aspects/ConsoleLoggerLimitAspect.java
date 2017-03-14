package com.gusev.spring.core.aspects;

import com.gusev.spring.core.beans.Event;
import com.gusev.spring.core.loggers.EventLogger;
import com.gusev.spring.core.loggers.FileEventLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Alexander on 28.02.2017.
 */
@Aspect
@Component
public class ConsoleLoggerLimitAspect {
    private final int MAX_COUNT;
    private int count = 0;
    private EventLogger eventLogger;

    @Autowired
    ConsoleLoggerLimitAspect(@Qualifier("fileEventLogger") EventLogger eventLogger, @Value("${console.limit:2}") int MAX_COUNT) {
        this.eventLogger = eventLogger;
        this.MAX_COUNT = MAX_COUNT;
    }

    @Pointcut("execution(* *.logEvent(..)) && within(com.gusev.spring.core.loggers.ConsoleEventLogger)")
    private void consoleLogEventMethods() {
    }


    @Around("consoleLogEventMethods() && args(event)")
    public void aroundLogEvent(ProceedingJoinPoint joinPoint, Event event) throws Throwable {

        if (count < MAX_COUNT) {
            joinPoint.proceed(new Object[]{event});
            count++;
        } else {
            System.out.println("ConsoleEventLogger max count is reached. Logging to " + eventLogger.getName());
            eventLogger.logEvent(event);
        }
    }
}