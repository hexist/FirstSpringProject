package com.gusev.spring.core.aspects;

import com.gusev.spring.core.loggers.CacheFileEventLogger;
import com.gusev.spring.core.loggers.CombinedEventLogger;
import com.gusev.spring.core.loggers.ConsoleEventLogger;
import org.aspectj.lang.JoinPoint;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestStatisticsAspect {

    @Test
    public void testCount() {
        StatisticsAspect aspect = new StatisticsAspect();
        JoinPoint joinPoint = mock(JoinPoint.class);

        when(joinPoint.getTarget()).thenReturn(new ConsoleEventLogger())
                .thenReturn(new CacheFileEventLogger())
                .thenReturn(new CombinedEventLogger())
                .thenReturn(new CacheFileEventLogger());

        aspect.count(joinPoint);
        aspect.count(joinPoint);
        aspect.count(joinPoint);
        aspect.count(joinPoint);

        verify(joinPoint, atMost(4)).getTarget();

        Map<Class<?>, Integer> mapCounter = aspect.getMapCounter();
        assertEquals(3, mapCounter.size());
        assertEquals(1, mapCounter.get(ConsoleEventLogger.class).intValue());
        assertEquals(2, mapCounter.get(CacheFileEventLogger.class).intValue());
        assertEquals(1, mapCounter.get(CombinedEventLogger.class).intValue());
    }

}