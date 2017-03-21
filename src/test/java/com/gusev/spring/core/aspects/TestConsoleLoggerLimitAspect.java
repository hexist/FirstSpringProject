package com.gusev.spring.core.aspects;

import com.gusev.spring.core.beans.Event;
import com.gusev.spring.core.loggers.EventLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TestConsoleLoggerLimitAspect {
    @Test
    public void testAroundLogEvent() throws Throwable{

        EventLogger mockLogger = mock(EventLogger.class);
        Event mockEvent = mock(Event.class);
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

        when(mockLogger.getName()).thenReturn("Mock Logger");

        ConsoleLoggerLimitAspect aspect = new ConsoleLoggerLimitAspect(mockLogger, 2);

        aspect.aroundLogEvent(joinPoint, mockEvent);
        aspect.aroundLogEvent(joinPoint, mockEvent);
        aspect.aroundLogEvent(joinPoint, mockEvent);

        verify(joinPoint, atMost(2)).proceed();
        verify(mockLogger, times(1)).logEvent(mockEvent);
    }
}