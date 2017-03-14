package com.gusev.spring.core;

import com.gusev.spring.core.aspects.StatisticsAspect;
import com.gusev.spring.core.beans.Client;
import com.gusev.spring.core.beans.Event;
import com.gusev.spring.core.beans.EventType;
import com.gusev.spring.core.configs.AppConfig;
import com.gusev.spring.core.configs.LogConfig;
import com.gusev.spring.core.loggers.EventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class App {

    @Autowired
    private Client client;

    @Value("#{T(com.gusev.spring.core.beans.Event).isDay(9, 21)? cacheFileEventLogger : consoleEventLogger}")
    private EventLogger defaultLogger;

    @Resource(name = "mapLoggers")
    private Map<EventType, EventLogger> mapLoggers;

    @Value("#{'Good ' + (T(com.gusev.spring.core.beans.Event).isDay(9, 21) ? 'day, ' : 'night, ') + " +
            "(systemProperties['os.name'].contains('Linux') ? systemEnvironment['USER'] : " +
            "systemEnvironment['USERNAME']) + '. Default logger is ' + app.defaultLogger.name}")
    private String firstMessage;

    @Autowired
    StatisticsAspect statisticsAspect;

    public App() {
    }

    public App(Client client, EventLogger eventLogger, Map<EventType, EventLogger> mapLoggers) {
        this.client = client;
        this.defaultLogger = eventLogger;
        this.mapLoggers = mapLoggers;
    }

    public EventLogger getDefaultLogger() {
        return defaultLogger;
    }


    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class, LogConfig.class);
        context.scan("com.gusev.spring.core");
        context.refresh();

        App app = (App) context.getBean("app");

        System.out.println(app.firstMessage);

        Client client = context.getBean(Client.class);
        System.out.println("Client: " + client.getGreeting() +
                "\nMy name is " + client.getFullname() +
                ", my id is " + client.getId());

        Event event = (Event) context.getBean("event");
        app.logEvent(event, "1 change his id on name", EventType.INFO);

        event = context.getBean(Event.class);
        app.logEvent(event, "2 not change", null);

        event = context.getBean(Event.class);
        app.logEvent(event, "3 not change", EventType.ERROR);

        app.printLogCounter();

        context.close();
    }

    private void printLogCounter() {
        if (statisticsAspect != null){
            System.out.println("Statistics: Number of calls each logger.");
            for(Map.Entry<Class<?>, Integer> entry: statisticsAspect.getMapCounter().entrySet()){
                System.out.println("---"  + entry.getKey().getSimpleName() + "---" + entry.getValue());
            }
        }
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
