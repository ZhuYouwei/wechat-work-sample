package com.example.wechatwork.controller;

import com.example.wechatwork.AppState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Controller
public class ToggleFunctionsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ToggleFunctionsController.class);

    @Autowired
    private AppState appState;

    // Sorry these should be with PUT request but this is hackathon!
    @GetMapping("/scheduler/on")
    public ResponseEntity<?> turnOnScheduler() {
        appState.setSchedulerToggle(true);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    @GetMapping("/scheduler/off")
    public ResponseEntity<?> turnOffcheduler() {
        appState.setSchedulerToggle(false);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/scheduler/is_on")
    public ResponseEntity<?> getSchedulerStatus() {
        HashMap<String, Boolean> hm = new HashMap<>();
        hm.put("is_on", appState.getSchedulerToggle());
        return new ResponseEntity<>(hm, HttpStatus.OK);
    }

    @GetMapping("/scheduler/lastrun")
    public ResponseEntity<?> getLastSchedulerRun(){
        LocalDateTime lastRunDate = appState.getLastSchedulerRunTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (lastRunDate == null) return new ResponseEntity<>(null, HttpStatus.OK);

        HashMap<String, String> hm = new HashMap<>();
        hm.put("lastRunDateTime", lastRunDate.format(formatter));

        return new ResponseEntity<>(hm, HttpStatus.OK);
    }

}