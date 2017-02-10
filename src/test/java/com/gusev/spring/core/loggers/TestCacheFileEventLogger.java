package com.gusev.spring.core.loggers;

import com.gusev.spring.core.beans.Event;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.plugin2.message.EventMessage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class TestCacheFileEventLogger {

    private File file;

    @Before
    public void createFile()throws IOException{
        this.file = File.createTempFile("test", "CacheFileEventLogger");
    }

    @After
    public void deleteFile(){
        file.delete();
    }

    @Test
    public void testLogEvent() throws IOException {
        Event event = new Event(new Date(), DateFormat.getDateInstance());
        CacheFileEventLogger cacheFileEventLogger = new CacheFileEventLogger(file.getAbsolutePath(), 2);
        cacheFileEventLogger.init();

        String content = FileUtils.readFileToString(this.file, Charset.defaultCharset());
        assertTrue("File is empty after initialization", content.isEmpty());

        cacheFileEventLogger.logEvent(event);

        content = FileUtils.readFileToString(this.file, Charset.defaultCharset());
        assertTrue("File is empty as events in cache", content.isEmpty());

        cacheFileEventLogger.logEvent(event);

        content = FileUtils.readFileToString(this.file, Charset.defaultCharset());
        assertFalse("File not empty, cache was dumped", content.isEmpty());
    }

    @Test
    public void testDestroy() throws IOException {
        Event event = new Event(new Date(), DateFormat.getDateInstance());
        CacheFileEventLogger cacheFileEventLogger = new CacheFileEventLogger(file.getAbsolutePath(), 2);
        cacheFileEventLogger.init();

        String content = FileUtils.readFileToString(this.file, Charset.defaultCharset());
        assertTrue("File is empty after initialization", content.isEmpty());

        cacheFileEventLogger.logEvent(event);

        content = FileUtils.readFileToString(this.file, Charset.defaultCharset());
        assertTrue("File is empty as events in cache", content.isEmpty());

        cacheFileEventLogger.destroy();

        content = FileUtils.readFileToString(this.file, Charset.defaultCharset());
        assertFalse("File not empty, cache was dumped", content.isEmpty());
    }
}