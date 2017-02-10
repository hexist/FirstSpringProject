package com.gusev.spring.core;

import com.gusev.spring.core.beans.Client;
import com.gusev.spring.core.configs.AppConfig;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import static org.junit.Assert.assertEquals;

public class TestContext {

    @Test
    public void testPropertyPlaceholderSystemOverride() {
        System.setProperty("id", "3");
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();

        Client client = ctx.getBean(Client.class);
        ctx.close();

        assertEquals("3", client.getId());
    }
}
