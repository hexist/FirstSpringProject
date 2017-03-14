package com.gusev.spring.core.loggers;

import com.gusev.spring.core.beans.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConsoleEventLogger extends AbstractEventLogger {

    @Override
    public void logEvent(Event event){
        System.out.println(event.toString());
    }

    @Value("Console Event Logger")
    @Override
    protected void setName(String name) {
        this.name = name;
    }
}
