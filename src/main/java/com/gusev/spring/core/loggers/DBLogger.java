package com.gusev.spring.core.loggers;

import com.gusev.spring.core.aspects.StatisticsAspect;
import com.gusev.spring.core.beans.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component("dataBaseLogger")
public class DBLogger extends AbstractEventLogger {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    StatisticsAspect statisticAspect;

    @PostConstruct
    public void init() {
        createSchema();
        createTableEventsIfNotExists();
        createTableLastEventsIfNotExists();
        updateEventId();
    }

    @PreDestroy
    public void destroy() {
        int total = getTotalEvents();
        System.out.println("Total events saved in your DB: " + total);

        List<Event> events = getAllEvents();
        String allIDs = events.stream()
                .map(Event::getId)
                .map(String::valueOf)
                .collect(Collectors.joining("; "));
        System.out.println("Event ids in DB: " + allIDs);

        setCurrentMaxId();
    }


    private void createSchema() {
        try {
            jdbcTemplate.update("CREATE SCHEMA IF NOT EXISTS postgres");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createTableEventsIfNotExists() {
        try {
            jdbcTemplate.update("CREATE TABLE IF NOT EXISTS events(" +
                    "id INTEGER NOT NULL PRIMARY KEY," +
                    "date DATE," +
                    "msg VARCHAR(100))");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //In this table we must save the ids of the last events that will be
    // used to create the AUTO_ID for further work.
    private void createTableLastEventsIfNotExists() {
        jdbcTemplate.update("CREATE TABLE IF NOT EXISTS lastEvents(" +
                "id SERIAL PRIMARY KEY," +
                "event_id INTEGER NOT NULL)");
    }

    private void updateEventId() {
        int currentMaxId = getCurrentMaxId();
        Event.setAutoId(currentMaxId + 1);
        System.out.println("Now last event id is: " + currentMaxId);
    }

    private int getCurrentMaxId() {
        Integer currentMaxId = jdbcTemplate.queryForObject("SELECT max(event_id) FROM lastEvents", Integer.class);
        return currentMaxId != null ? currentMaxId : 0;
    }

    private void setCurrentMaxId(){
        jdbcTemplate.update("INSERT INTO lastEvents (event_id) VALUES (?)", statisticAspect.getMaxId());
    }

    public int getTotalEvents() {
        Integer total = jdbcTemplate.queryForObject("SELECT count(id) FROM events", Integer.class);
        return total != null ? total : 0;
    }

    public List<Event> getAllEvents() {
        List<Event> allEvents = jdbcTemplate.query("SELECT * FROM events", (resultSet, i) -> {
            Integer id = resultSet.getInt("id");
            Date date = resultSet.getDate("date");
            String msg = resultSet.getString("msg");
            return new Event(id, date, msg);
        });

        return allEvents;
    }

    @Override
    public void logEvent(Event event) {
        jdbcTemplate.update("INSERT INTO events (id, date, msg) VALUES (?, ?, ?)", event.getId(), event.getDate(), event.getMsg());
        System.out.println("Event saved to DB with id: " + event.getId());
    }

    @Value("Data Base Logger")
    @Override
    protected void setName(String name) {
        this.name = name;
    }
}
