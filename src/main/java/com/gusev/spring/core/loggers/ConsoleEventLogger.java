package com.gusev.spring.core.loggers;

import com.gusev.spring.core.beans.Event;

/**
 * Created by Alexander on 15.12.2016.
 */
public class ConsoleEventLogger implements EventLogger {
    public void logEvent(Event event){
         System.out.println(event.toString());
    }
}
