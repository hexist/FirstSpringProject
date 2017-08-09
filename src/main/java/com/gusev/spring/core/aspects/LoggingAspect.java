package com.gusev.spring.core.aspects;

import com.gusev.spring.core.beans.Event;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {

    private static Logger log = Logger.getLogger(LoggingAspect.class.getName());

    /*
    execution - method execution join points.
    * - any ret-type, * - any class, method logEvent().
    Method name becomes the name of pointcut.
    */
    @Pointcut("execution(* *.logEvent(..))")
    private void logEventMethods() {
    }

//    //join points within certain types(ex. FileLoggers)
//    @Pointcut("allLogEventMethods() && within(*File*Logger)")
//    private void logEventInsideFileLoggers() {
//    }

    @Before("logEventMethods()")
    public void logBefore(JoinPoint joinPoint){

        /*
        getTarget - returns the type of the object in which the method is called
        getSignature - returns method signature
         */
        log.info("BEFORE: " + joinPoint.getTarget().getClass().getSimpleName() + " "
        + joinPoint.getSignature().getName());
    }

    @AfterReturning(
            pointcut = "logEventMethods()",
            returning = "returnValue"
    )
    public void logAfter(JoinPoint joinPoint, Object returnValue){
        log.info(joinPoint.getTarget().getClass().getSimpleName() + " RETURN: " + returnValue);
    }

    @AfterThrowing(
            pointcut = "logEventMethods()",
            throwing = "exception"
    )
    public void logAfterThrow(Throwable exception){
        log.warning("Exception: " + exception);
    }
}
