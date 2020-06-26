package com.example.wechatwork.controller;

import com.example.wechatwork.Job.ScheduledDisclaimerBroadcastJob;
import com.example.wechatwork.MemoryStorage;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.util.crypto.WxCryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class AttestationController {
    @Autowired
    private ScheduledDisclaimerBroadcastJob job;

    @Autowired
    private MemoryStorage store;

    // Lets make it all GET request for quick and dirty
    @GetMapping("/attestation/send")
    public ResponseEntity<?> sendAttestation() {
        log.info("Got send attestation requests");
        job.sendAttestations();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/attestation/current")
    public ResponseEntity<?> getAllRemainedAttestations(){
        log.info("Get attestation requests");
        return new ResponseEntity<>(store.getStore(), HttpStatus.OK);
    }
}
