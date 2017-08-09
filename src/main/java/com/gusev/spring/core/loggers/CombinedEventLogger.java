package com.gusev.spring.core.loggers;

import com.gusev.spring.core.beans.Event;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Component
public class CombinedEventLogger extends AbstractEventLogger{

    @Resource(name = "combinedLoggers")
    private List<EventLogger> loggers;

    @Override
    public void logEvent(Event event) {
        for (EventLogger eventLogger : loggers) {
            eventLogger.logEvent(event);
        }
    }

    public List<EventLogger> getLoggers() {
        return Collections.unmodifiableList(loggers);
    }

    @Value("#{'Combined ' + combinedLoggers.![name].toString()}")
    @Override
    protected void setName(String name) {
        this.name = name;
    }
}
