package com.example.wechatwork;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AppState {
    private Boolean schedulerToggle = true;
    private LocalDateTime lastSchedulerRunTime = null;

    public Boolean getSchedulerToggle() {
        return schedulerToggle;
    }

    public void setSchedulerToggle(Boolean schedulerToggle) {
        this.schedulerToggle = schedulerToggle;
    }

    public LocalDateTime getLastSchedulerRunTime() {
        return lastSchedulerRunTime;
    }

    public void setLastSchedulerRunTime(LocalDateTime lastSchedulerRunTime) {
        this.lastSchedulerRunTime = lastSchedulerRunTime;
    }
}
