package com.gusev.spring.core;

import com.gusev.spring.core.beans.Client;
import com.gusev.spring.core.beans.Event;
import com.gusev.spring.core.beans.EventType;
import com.gusev.spring.core.loggers.EventLogger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * Created by Alexander on 15.12.2016.
 */
public class App {
    private Client client;
    private EventLogger defaultLogger;
    private Map<EventType, EventLogger> mapLoggers;

    public App(){

    }

    public App(Client client, EventLogger eventLogger, Map<EventType, EventLogger> mapLoggers) {
        super();
        this.client = client;
        this.defaultLogger = eventLogger;
        this.mapLoggers = mapLoggers;
    }


    public static void main(String[] args) {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        App app = (App) context.getBean("app");

        Event event = (Event) context.getBean("event");
        app.logEvent(event, "1 change his id on name and use the destroy-method of CacheLogger", null);

        event = context.getBean(Event.class);
        app.logEvent(event, "2 not change", EventType.INFO);

        event = context.getBean(Event.class);
        app.logEvent(event, "3 not change", EventType.ERROR);

        context.close();
    }

    private void logEvent(Event event, String msg, EventType eventType){
        String message = msg.replaceAll(client.getId(), client.getFullname());
        event.setMsg(message + ". " + client.getGreeting());

        EventLogger logger = mapLoggers.get(eventType);
        if(logger == null){
            logger = defaultLogger;
        }
        logger.logEvent(event);
    }
}
