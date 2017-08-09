package com.gusev.spring.core;

import com.gusev.spring.core.beans.Client;
import com.gusev.spring.core.beans.Event;
import com.gusev.spring.core.configs.AppConfig;
import com.gusev.spring.core.configs.DBConfig;
import com.gusev.spring.core.configs.LogConfig;
import com.gusev.spring.core.loggers.CacheFileEventLogger;
import com.gusev.spring.core.loggers.CombinedEventLogger;
import com.gusev.spring.core.loggers.EventLogger;
import com.gusev.spring.core.loggers.FileEventLogger;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestContext {

    @Test
    public void testPropertyPlaceholderSystemOverride() {
        System.setProperty("id", "3");
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();

        Client client = ctx.getBean(Client.class);
        ctx.close();

        assertEquals("3", client.getId());
    }

    @Test
    public void testFileEventLoggerDefaultValue() throws IOException {
        File file = new File("target/log.txt");
        assertFileEventLogger(file);
    }

    private void assertFileEventLogger(File file) throws IOException {
        System.setProperty("log.file", file.getAbsolutePath());

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(LogConfig.class);
        context.scan(FileEventLogger.class.getPackage().getName());
        context.refresh();

        EventLogger logger = context.getBean("fileEventLogger", FileEventLogger.class);
        Event event = new Event();

        String uid = UUID.randomUUID().toString();

        event.setMsg(uid);
        logger.logEvent(event);

        context.close();

        String str = FileUtils.readFileToString(file, Charset.defaultCharset());
        assertTrue(str.contains(uid));
    }

    @Test
    public void testLoggersNames() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(LogConfig.class, DBConfig.class);
        context.scan(FileEventLogger.class.getPackage().getName());
        context.refresh();

        EventLogger fileLogger = context.getBean("fileEventLogger", FileEventLogger.class);
        EventLogger cacheLogger = context.getBean("cacheFileEventLogger", CacheFileEventLogger.class);
        CombinedEventLogger combinedLogger = context.getBean(CombinedEventLogger.class);

        assertEquals(fileLogger.getName() + " uses cache", cacheLogger.getName());

        Collection<String> combinedNames = combinedLogger.getLoggers().stream()
                .map(v -> v.getName()).collect(Collectors.toList());

        assertEquals("Combined " + combinedNames, combinedLogger.getName());

        context.close();
    }
}

