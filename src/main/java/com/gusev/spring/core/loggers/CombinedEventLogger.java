package com.gusev.spring.core.loggers;

import com.gusev.spring.core.beans.Event;

import java.util.List;

/**
 * Created by Alexander on 04.01.2017.
 */
public class CombinedEventLogger implements EventLogger {
    List<EventLogger> loggers;

    CombinedEventLogger(List<EventLogger> loggers){
        this.loggers = loggers;
    }

    public void logEvent(Event event) {
        for (EventLogger eventLogger : loggers) {
            eventLogger.logEvent(event);
        }
    }
}
