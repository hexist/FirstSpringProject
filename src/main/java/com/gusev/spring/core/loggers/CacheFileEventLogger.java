package com.gusev.spring.core.loggers;

import com.gusev.spring.core.beans.Event;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alexander on 02.01.2017.
 */
public class CacheFileEventLogger extends FileEventLogger {
    int cacheSize;
    List<Event> cache = new LinkedList<Event>();

    public CacheFileEventLogger(String filename, int cacheSize){
        super(filename);
        this.cacheSize = cacheSize;
    }

    public void logEvent(Event event){
        cache.add(event);

        if(cache.size() == cacheSize){
            writeEventsFromCache();
            cache.clear();
        }
    }

    public void writeEventsFromCache(){
        for(Event event: cache){
            super.logEvent(event);
        }
    }

    public void destroy(){
        if(!cache.isEmpty())
            writeEventsFromCache();
    }
}

