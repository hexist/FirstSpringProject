package com.gusev.spring.core.loggers;

public abstract class AbstractEventLogger implements EventLogger {
    public String name;

    @Override
    public String getName() {
        return name;
    }

    protected abstract void setName(String name);
}
