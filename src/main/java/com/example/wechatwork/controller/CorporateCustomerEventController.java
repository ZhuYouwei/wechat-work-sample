package com.example.wechatwork.controller;

import com.example.wechatwork.MemoryStorage;
import com.example.wechatwork.config.WechatWorkConfig;
import com.example.wechatwork.gateway.WechatWorkGateway;
import com.example.wechatwork.model.AttestUserInfo;
import com.example.wechatwork.model.GetTokenResponse;
import lombok.val;
import me.chanjar.weixin.common.util.XmlUtils;
import me.chanjar.weixin.common.util.crypto.WxCryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Controller
public class CorporateCustomerEventController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CorporateCustomerEventController.class);

    @Autowired
    private WechatWorkConfig wechatWorkConfig;
    @Autowired
    private WechatWorkGateway gw;

    @Autowired
    private MemoryStorage store;

    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        return new ResponseEntity<>("pong", HttpStatus.OK);
    }

    @GetMapping("/corporate-customer-event")
    public ResponseEntity<?> echo(@RequestParam("msg_signature") String msg_signature,
                                  @RequestParam("timestamp") String timestamp,
                                  @RequestParam("nonce") String nonce,
                                  @RequestParam("echostr") String echostr) {
        WxCryptUtil wxCryptUtil = new WxCryptUtil(wechatWorkConfig.getExternalContactToken(),
                wechatWorkConfig.getExternalContactAesKey(),
                wechatWorkConfig.getCorpid());

        String decrypt = wxCryptUtil.decrypt(echostr);

        return new ResponseEntity<>(decrypt, HttpStatus.OK);
    }


    @PostMapping(path = "/corporate-customer-event")
    public ResponseEntity<?> callback(@RequestParam("msg_signature") String message,
                                      @RequestParam("nonce") String nonce,
                                      @RequestBody String eventString){

        WxCryptUtil wxCryptUtil = new WxCryptUtil(wechatWorkConfig.getExternalContactToken(),
                wechatWorkConfig.getExternalContactAesKey(),
                wechatWorkConfig.getCorpid());

        val msgEnvelope = XmlUtils.xml2Map(eventString);

        String encryptedXml = String.valueOf(msgEnvelope.get("Encrypt"));
        String decryptedXml = wxCryptUtil.decrypt(encryptedXml);

        val msgContent = XmlUtils.xml2Map(decryptedXml);

        System.out.println("********** New Message received **************");
        msgContent.forEach((k, v) -> System.out.println(k + " : " + v));

        if ("change_external_contact".equals(msgContent.get("Event"))) {
            if ("add_external_contact".equals(msgContent.get("ChangeType"))){
                GetTokenResponse res = gw.getAccessToken();
                // Welcome message
                String r1 = gw.sendWelcome(res.getAccess_token(), (String) msgContent.get("WelcomeCode"), "Welcome to JPMorgan!");
                String externalUserId = (String) msgContent.get("ExternalUserID");

                // Disclaimer message
                String r2 = gw.sendMessageTask(res.getAccess_token(), externalUserId, "Some disclaimer text.");
            }
        } else if ("taskcard_click".equals(msgContent.get("Event"))) {
            String taskId = (String) msgContent.get("TaskId");
            String eventKey = (String) msgContent.get("EventKey");

            if ("affirm".equalsIgnoreCase(eventKey)) {
//                store.removeTaskId(taskId);
                LOGGER.info("Log affirmed for {}", taskId);
                AttestUserInfo info = store.getInfoByTaskId(taskId);
                if (info != null) {
                    info.setAttestEndDatetime(LocalDateTime.now());
                    info.setAttested(true);
                }
            }
        }

        return new ResponseEntity<>("reply", HttpStatus.OK);
    }

}