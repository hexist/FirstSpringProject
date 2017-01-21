package com.gusev.spring.core.loggers;

import com.gusev.spring.core.beans.Event;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Configuration;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Alexander on 19.01.2017.
 */
public class TestConsoleEventLogger {

    private static final String MSG = "My String";
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private PrintStream stdout;

    @Before
    public void setUpStreams(){
        stdout = System.out;
        System.setOut(new PrintStream(outputStream));
    }


    @After
    public void cleanUpSreams(){
        System.setOut(stdout);
    }

    @Test
    public void testLogEvent(){
        ConsoleEventLogger consoleEventLogger = new ConsoleEventLogger();
        Event event = new Event(new Date(), DateFormat.getDateInstance());
        event.setMsg(MSG);

        consoleEventLogger.logEvent(event);

        assertTrue(outputStream.toString(Charset.defaultCharset()).contains(MSG));

        assertEquals(event.toString().trim(), outputStream.toString(Charset.defaultCharset()).trim());
    }
}
