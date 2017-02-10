package com.gusev.spring.core.loggers;

import com.gusev.spring.core.beans.Event;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class TestFileEventLogger {

    private File file;

    @Before
    public void createFile() throws IOException {
        this.file = File.createTempFile("test", "FileEventLogger");
    }

    @After
    public void deleteFile() {
        file.delete();
    }

    @Test
    public void testInit() throws IOException {
        FileEventLogger fileEventLogger = new FileEventLogger(file.getAbsolutePath());
        fileEventLogger.init();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitFail() throws IOException {
        file.setReadOnly();
        FileEventLogger fileEventLogger = new FileEventLogger(file.getAbsolutePath());
        fileEventLogger.init();
    }

    @Test
    public void testLogEvent() throws IOException {
        Event event = new Event(new Date(), DateFormat.getDateInstance());
        FileEventLogger fileEventLogger = new FileEventLogger(file.getAbsolutePath());
        fileEventLogger.init();

        String content = FileUtils.readFileToString(file, Charset.defaultCharset());
        assertTrue(content.isEmpty());

        fileEventLogger.logEvent(event);
        content = FileUtils.readFileToString(file, Charset.defaultCharset());
        assertFalse(content.isEmpty());
    }
}