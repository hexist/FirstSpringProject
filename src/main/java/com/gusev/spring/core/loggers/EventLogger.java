package com.gusev.spring.core.loggers;

import com.gusev.spring.core.beans.Event;

public interface EventLogger {
     void logEvent(Event event);

     String getName();
}
