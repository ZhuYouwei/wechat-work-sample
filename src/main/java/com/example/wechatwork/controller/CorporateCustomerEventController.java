package com.example.wechatwork.controller;

import com.example.wechatwork.MemoryStorage;
import com.example.wechatwork.config.WechatWorkConfig;
import com.example.wechatwork.gateway.WechatWorkGateway;
import com.example.wechatwork.model.AttestUserInfo;
import com.example.wechatwork.model.GetTokenResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.chanjar.weixin.common.util.XmlUtils;
import me.chanjar.weixin.common.util.crypto.WxCryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class CorporateCustomerEventController {

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
                                      @RequestBody String eventString) {

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
            if ("add_external_contact".equals(msgContent.get("ChangeType"))) {
                val externalUserID = (String) msgContent.get("ExternalUserID");
                GetTokenResponse res = gw.getAccessToken();

                String contactName = gw.fetchExternalUserDetail(res.getAccess_token(), externalUserID);

                log.info("external user contact name {}", contactName);

                // Welcome message
                String r1 = gw.sendWelcome(res.getAccess_token(), (String) msgContent.get("WelcomeCode"), "Welcome to JPMorgan! " + contactName);

                log.info("Welcome message call back response {}", r1);

                // Disclaimer message
                String r2 = gw.sendMessageTask(res.getAccess_token(), externalUserID, "Some disclaimer text.");

                log.info("Disclaimer message call back response {}", r2);
            }

            // 1st case is Employee deletes client
            // 2nd case is Client deletes Employee
            if ("del_external_contact".equalsIgnoreCase((String) msgContent.get("ChangeType")) ||
                    "del_follow_user".equalsIgnoreCase((String) msgContent.get("ChangeType"))) {
                String userId = (String) msgContent.get("UserID");
                GetTokenResponse res = gw.getAccessToken();

//                BigDecimal externalClientCount = this.gw.fetchExternalContactCount(res.getAccess_token(), userId);
                List<String> externalContact = this.gw.fetchExternalContactCount(res.getAccess_token(), userId);

                GetTokenResponse appToken = gw.getAccessTokenForUserApp();
//
//                String externalUserName = gw.fetchExternalUserDetail(res.getAccess_token(),(String) msgContent.get("ExternalUserID"));
//
//                log.info("external user contact name {}", externalUserName);

                String userMsg = "You are no longer WeChat contact with an external client, current external users: " + externalContact.toString();

                String response = gw.sendAppMessage(appToken.getAccess_token(), userMsg, userId);

                log.info("Delete client callback response, {}", response);
            }
        } else if ("taskcard_click".equals(msgContent.get("Event"))) {
            String taskId = (String) msgContent.get("TaskId");
            String eventKey = (String) msgContent.get("EventKey");

            if ("affirm".equalsIgnoreCase(eventKey)) {
//                store.removeTaskId(taskId);
                log.info("Log affirmed for {}", taskId);
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