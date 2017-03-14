package com.gusev.spring.core.beans;

public class Client {

    private String id;
    private String fullName;


    private String greeting;

    public Client(String id, String fullName) {
        super();
        this.id = id;
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullName;
    }

    public void setFullname(String fullname) {
        this.fullName = fullname;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String gr) {
        this.greeting = gr;
    }
}
