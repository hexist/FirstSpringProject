package com.gusev.spring.core;

import com.gusev.spring.core.beans.Client;
import com.gusev.spring.core.beans.Event;
import com.gusev.spring.core.loggers.ConsoleEventLogger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by Alexander on 19.01.2017.
 */
public class TestContext {

    @Test
    public void testPropertyPlaceholderSystemOverride(){
        System.setProperty("id", "3");
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        Client client = context.getBean(Client.class);
        context.close();

        Assert.assertEquals("3", client.getId());
    }
}
