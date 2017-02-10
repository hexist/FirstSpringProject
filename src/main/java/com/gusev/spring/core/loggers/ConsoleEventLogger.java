package com.gusev.spring.core.loggers;

import com.gusev.spring.core.beans.Event;
import org.springframework.stereotype.Component;

@Component
public class ConsoleEventLogger implements EventLogger {

    @Override
    public void logEvent(Event event){
         System.out.println(event.toString());
    }
}
