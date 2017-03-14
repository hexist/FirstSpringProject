package com.gusev.spring.core.loggers;

import com.gusev.spring.core.beans.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;

import java.util.List;


@Component
public class CacheFileEventLogger extends FileEventLogger {

    //System property cache.size or 3 if property is not set
    @Value("${cache.size:3}")
    int cacheSize;

    List<Event> cache;

    public CacheFileEventLogger() {
    }

    public CacheFileEventLogger(String filename, int cacheSize){
        super(filename);
        this.cacheSize = cacheSize;
    }

    @PostConstruct
    public  void initCacheLogger(){
        this.cache = new ArrayList<Event>(cacheSize);
    }

    @PreDestroy
    public void destroy(){
        if(!cache.isEmpty())
            writeEventsFromCache();
        cache.clear();
    }

    @Override
    public void logEvent(Event event){
        cache.add(event);

        if(cache.size() == cacheSize){
            writeEventsFromCache();
            cache.clear();
        }
    }

    public void writeEventsFromCache(){
        for(Event event: cache) super.logEvent(event);
    }

    @Value("#{fileEventLogger.name + ' uses cache'}")
    @Override
    protected void setName(String name) {
        super.setName(name);
    }
}

