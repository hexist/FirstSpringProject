package com.gusev.spring.core.beans;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class TestEvent {

    Date date = null;
    DateFormat dateFormat = null;
    Event event = null;

    @Before
    public void init(){
        date = new Date();
        dateFormat = DateFormat.getDateInstance();

        event = new Event(date, dateFormat);
    }

    @Test
    public void testToString(){
        String str = event.toString();
        assertTrue(str.contains(dateFormat.format(date)));
    }

    @Test
    public void testToString2(){
        String str = event.toString();
        assertTrue(str.contains(dateFormat.format(date)));
    }

    @Test
    public void testGetDate(){
        assertEquals(date, event.getDate());
    }

}