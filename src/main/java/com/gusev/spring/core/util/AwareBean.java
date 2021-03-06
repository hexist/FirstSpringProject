package com.gusev.spring.core.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AwareBean implements ApplicationContextAware, ApplicationEventPublisherAware, BeanNameAware {

    String name;
    ApplicationContext applicationContext;
    ApplicationEventPublisher eventPublisher;

    @PostConstruct
    public void init() {
        System.out.println(this.getClass().getSimpleName() + " --> My name is: " + name);
        if (applicationContext != null) {
            System.out.println(this.getClass().getSimpleName() + " --> My context is: " + applicationContext);
        } else {
            System.out.println(this.getClass().getSimpleName() + " --> Context isn't set.");
        }
        if(eventPublisher != null){
            System.out.println(this.getClass().getSimpleName() + " --> My eventPublisher is: " + eventPublisher);
        }else{
            System.out.println(this.getClass().getSimpleName() + " --> eventPublisher isn't set.");
        }
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
}
