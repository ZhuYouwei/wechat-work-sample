package com.example.wechatwork.controller;

import com.example.wechatwork.config.WechatWorkConfig;
import com.example.wechatwork.gateway.WechatWorkGateway;
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

@Controller
@Slf4j
public class CorporateCustomerEventController {

    @Autowired
    private WechatWorkConfig wechatWorkConfig;
    @Autowired
    private WechatWorkGateway gw;

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
                //TODO fetch external user name and send customized welcome message


                GetTokenResponse res = gw.getAccessToken();
                // Welcome message
                String r1 = gw.sendWelcome(res.getAccess_token(), (String) msgContent.get("WelcomeCode"), "Welcome to JPMorgan!");

                log.info("Welcome message call back response {}",r1);

                String externalUserId = (String) msgContent.get("ExternalUserID");

                // Disclaimer message
                String r2 = gw.sendMessageTask(res.getAccess_token(), externalUserId, "Some disclaimer text.");

                log.info("Disclaimer message call back response {}", r2);
            }

            //TODO event when do contact deletion, any trigger?
        }

        return new ResponseEntity<>("reply", HttpStatus.OK);
    }

}