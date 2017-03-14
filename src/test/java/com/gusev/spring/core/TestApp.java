package com.gusev.spring.core;

import com.gusev.spring.core.beans.Client;
import com.gusev.spring.core.beans.Event;
import com.gusev.spring.core.beans.EventType;
import com.gusev.spring.core.loggers.AbstractEventLogger;
import com.gusev.spring.core.loggers.EventLogger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TestApp {

    private static final String MSG = "Message";

    @Test
    public void testClientNameReplacement() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Client client = new Client("19", "Alex");
        FictitiousLogger fictitiousLogger = new FictitiousLogger();

        App app = new App(client, fictitiousLogger, Collections.emptyMap());

        Event event = new Event(new Date(), DateFormat.getDateInstance());

        invokeLogEvent(app, event, MSG + "client id isn't here", null);
        assertTrue(fictitiousLogger.getEvent().getMsg().contains(MSG));
        assertFalse(fictitiousLogger.getEvent().getMsg().contains(client.getFullname()));

        invokeLogEvent(app, event, MSG + " " + client.getId(), null);
        assertTrue(fictitiousLogger.getEvent().getMsg().contains(MSG));
        assertTrue(fictitiousLogger.getEvent().getMsg().contains(client.getFullname()));
    }

    @Test
    public void testCorrectLoggerCall() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Client client = new Client("19", "Alex");
        FictitiousLogger defaultLogger = new FictitiousLogger();
        FictitiousLogger infoLogger = new FictitiousLogger();

//        Map<EventType, EventLogger> mapLoggers = new HashMap<>();
//        mapLoggers.put(EventType.INFO, infoLogger);
//
//        App app = new App(client, defaultLogger, mapLoggers);
        App app = new App(client, defaultLogger, new HashMap<EventType, EventLogger>(){{
            put(EventType.INFO, infoLogger);
        }});

        Event event = new Event(new Date(), DateFormat.getDateInstance());

        invokeLogEvent(app, event, MSG + " " + client.getId(), null);
        assertNull(infoLogger.getEvent());
        assertNotNull(defaultLogger.getEvent());

        infoLogger.setEvent(null);
        defaultLogger.setEvent(null);

//        Т.к. errorLogger отсутствует, то mapLoggers.get(EventType.ERROR) - выдаст null.
//        Соответственно будет использоваться defaultLogger.
        invokeLogEvent(app, event, MSG + " " + client.getId(), EventType.ERROR);
        assertNull(infoLogger.getEvent());
        assertNotNull(defaultLogger.getEvent());


        infoLogger.setEvent(null);
        defaultLogger.setEvent(null);

        invokeLogEvent(app, event, MSG + " " + client.getId(), EventType.INFO);
        assertNotNull(infoLogger.getEvent());
        assertNull(defaultLogger.getEvent());


    }


    private void invokeLogEvent(App app, Event event, String msg, EventType eventType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = app.getClass().getDeclaredMethod("logEvent", Event.class, String.class, EventType.class);
        method.setAccessible(true);
        method.invoke(app, event, msg, eventType);
    }

    private class FictitiousLogger extends AbstractEventLogger{
        private Event event;

        public void setEvent(Event event){
            this.event = event;
        }

        @Override
        public void logEvent(Event event) {
            this.event = event;
        }

        public Event getEvent(){
            return event;
        }

        @Value("Fictitious Logger")
        @Override
        protected void setName(String name) {
            this.name = name;
        }
    }
}
