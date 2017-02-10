package com.gusev.spring.core;

import com.gusev.spring.core.beans.Client;
import com.gusev.spring.core.beans.Event;
import com.gusev.spring.core.beans.EventType;
import com.gusev.spring.core.configs.AppConfig;
import com.gusev.spring.core.configs.LogConfig;
import com.gusev.spring.core.loggers.EventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class App {

    @Autowired
    private Client client;

    @Resource(name = "defaultLogger")
    private EventLogger defaultLogger;

    @Resource(name = "mapLoggers")
    private Map<EventType, EventLogger> mapLoggers;

    public App() {

    }

    public App(Client client, EventLogger eventLogger, Map<EventType, EventLogger> mapLoggers) {
        this.client = client;
        this.defaultLogger = eventLogger;
        this.mapLoggers = mapLoggers;
    }


    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class, LogConfig.class);
        context.scan("com.gusev.spring.core");
        context.refresh();

        App app = (App) context.getBean("app");

        Client client =  context.getBean(Client.class);
        System.out.println("Client: " + client.getGreeting() +
                            "\nMy name is " + client.getFullname() +
                            ", my id is " + client.getId());

        Event event = (Event) context.getBean("event");
        app.logEvent(event, "1 change his id on name", EventType.INFO);

        event = context.getBean(Event.class);
        app.logEvent(event, "2 not change", null);

        event = context.getBean(Event.class);
        app.logEvent(event, "3 not change", EventType.ERROR);

        context.close();
    }

    private void logEvent(Event event, String msg, EventType eventType) {
        String message = msg.replaceAll(client.getId(), client.getFullname());
        event.setMsg(message);

        EventLogger logger = mapLoggers.get(eventType);
        if (logger == null) {
            logger = defaultLogger;
        }
        logger.logEvent(event);
    }
}
