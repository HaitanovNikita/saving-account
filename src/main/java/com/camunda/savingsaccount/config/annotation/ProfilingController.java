package com.camunda.savingsaccount.config.annotation;

import org.springframework.stereotype.Component;

@Component
public class ProfilingController implements ProfilingControllerMBean {
    private boolean enable=true;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
