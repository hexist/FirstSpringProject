package com.gusev.spring.core;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Created by Alexander on 02.01.2017.
 */
public class Monitor implements ApplicationListener<ApplicationEvent> {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println(event.getClass().getSimpleName() + " > " + event.getSource().toString());
    }

}
