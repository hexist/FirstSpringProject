package com.gusev.spring.core.configs;

import com.gusev.spring.core.beans.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.text.DateFormat;
import java.util.Date;

@Configuration
@PropertySource("classpath:client.properties")
public class AppConfig {

    @Autowired
    private Environment environment;

    @Bean
    public Date newDate() {
        return new Date();
    }

    @Bean
    public Date fakeDate() {
        return new Date();
    }

    @Bean
    public DateFormat dateFormat() {
        return DateFormat.getDateInstance();
    }

    @Bean
    public Client client(){
        Client client = new Client(environment.getRequiredProperty("id"), environment.getRequiredProperty("name"));
        client.setGreeting(environment.getProperty("greeting"));
        return client;
    }
}
