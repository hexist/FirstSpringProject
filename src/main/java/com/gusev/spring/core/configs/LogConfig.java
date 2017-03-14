package com.gusev.spring.core.configs;

import com.gusev.spring.core.beans.EventType;
import com.gusev.spring.core.loggers.*;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ComponentScan("com.gusev.spring.core.loggers")
public class LogConfig {

    @Bean
    public static PropertyPlaceholderConfigurer propertyConfig(){
        PropertyPlaceholderConfigurer placeholder = new PropertyPlaceholderConfigurer();
        placeholder.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        return placeholder;
    }

    @Resource(name = "consoleEventLogger")
    private EventLogger consoleEventLogger;

    @Resource(name = "fileEventLogger")
    private EventLogger fileEventLogger;

    @Resource(name = "combinedEventLogger")
    private EventLogger combinedEventLogger;



    @Bean
    public List<EventLogger> combinedLoggers(){
        List<EventLogger> loggers = new ArrayList<EventLogger>(2);
        loggers.add(consoleEventLogger);
        loggers.add(fileEventLogger);
        return loggers;
    }

    @Bean
    public Map<EventType, EventLogger> mapLoggers(){
        Map<EventType, EventLogger> map = new HashMap<EventType, EventLogger>(2);
        map.put(EventType.INFO, consoleEventLogger);
        map.put(EventType.ERROR, combinedEventLogger);
        return map;
    }
}
