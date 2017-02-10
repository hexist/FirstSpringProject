package com.gusev.spring.core.loggers;

import com.gusev.spring.core.beans.Event;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component
public class CombinedEventLogger implements EventLogger {

    @Resource(name = "combinedLoggers")
    private List<EventLogger> loggers;

    @Override
    public void logEvent(Event event) {
        for (EventLogger eventLogger : loggers) {
            eventLogger.logEvent(event);
        }
    }
}
