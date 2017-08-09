package com.gusev.spring.core.loggers;


import com.gusev.spring.core.beans.Event;
import com.gusev.spring.core.configs.DBConfig;
import com.gusev.spring.core.configs.LogConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TestDBLogger {

    private AnnotationConfigApplicationContext ctx;
    private List<Event> events;

    @Before
    public void createContext() {
        ctx = new AnnotationConfigApplicationContext();
        ctx.register(LogConfig.class, DBConfig.class);
        ctx.scan(EventLogger.class.getPackage().getName(), Event.class.getPackage().getName());
        ctx.refresh();
    }


    @After
    public void cleanUp() {
        JdbcTemplate jdbc = ctx.getBean(JdbcTemplate.class);

        List<Integer> ids = events.stream().map(Event::getId).collect(Collectors.toList());

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbc);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);

        deleteTestLogs(namedParameterJdbcTemplate, parameters);

        ctx.close();

        deleteLastEventId(namedParameterJdbcTemplate, parameters);
        resetIdSequenceValue(jdbc);
    }

    private void deleteTestLogs(NamedParameterJdbcTemplate jdbcTemplate, SqlParameterSource parameters){
        String sql = "DELETE FROM postgres.events WHERE id IN (:ids)";
        jdbcTemplate.update(sql, parameters);
    }

    private void deleteLastEventId(NamedParameterJdbcTemplate jdbcTemplate, SqlParameterSource parameters){
        String sql2 = "DELETE FROM postgres.lastevents WHERE event_id IN (:ids)";
        jdbcTemplate.update(sql2, parameters);
    }

    private void resetIdSequenceValue(JdbcTemplate jdbc){
        String sql3 = "SELECT setval('lastevents_id_seq', (SELECT MAX(id) FROM lastevents))";
        jdbc.queryForObject(sql3, Integer.class);
    }

    @Test
    public void testLogEventAndGet() {
        DBLogger logger = ctx.getBean("dataBaseLogger", DBLogger.class);

        int total = logger.getTotalEvents();

        Event event1 = ctx.getBean(Event.class);
        event1.setMsg("test event 1");
        logger.logEvent(event1);

        assertEquals(total + 1, logger.getTotalEvents());

        Event event2 = ctx.getBean(Event.class);
        event2.setMsg("test event 2");
        logger.logEvent(event2);
        System.out.println(event2.toString());

        assertEquals(total + 2, logger.getTotalEvents());
        assertEquals(total + 2, logger.getAllEvents().size());

        events = Arrays.asList(event1, event2);
        assertTrue(logger.getAllEvents().containsAll(events));
    }
}
