package com.example.wechatwork.Job;


import com.example.wechatwork.MemoryStorage;
import com.example.wechatwork.config.WechatWorkConfig;
import com.example.wechatwork.model.AttestUserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Component
@Slf4j
public class ScheduledDisclaimerBroadcastJob {
    @Autowired
    private WechatWorkConfig config;

    @Autowired
    private MemoryStorage store;

    String DISCLAIMER_MSG = "J.P. Morgan Funds believes an informed advisor community means a better off investment community.\n" +
            "\n" +
            "As such, “let’s make every investor better off” and the communications associated with this marketing line are meant to showcase that J.P. Morgan Funds would like to partner with Financial Advisors to help them help their clients.\n" +
            "\n" +
            "This in no way is meant to be promissory regarding performance of products.\n" +
            "\n" +
            "J.P. Morgan Funds and J.P. Morgan ETFs are distributed by JPMorgan Distribution Services, Inc., which is an affiliate of JPMorgan Chase & Co. Affiliates of JPMorgan Chase & Co. receive fees for providing various services to the funds. JPMorgan Distribution Services, Inc. is a member of FINRA.  FINRA's BrokerCheck\n" +
            "\n" +
            "Investors should carefully consider the investment objectives and risks as well as charges and expenses of the JPMorgan ETF before investing. The summary and full prospectuses contain this and other information about the ETF. Read the prospectus carefully before investing. Call 1-844-4JPMETF or visit www.jpmorganETFs.com to obtain a prospectus.\n" +
            "\n" +
            "Contact JPMorgan Distribution Services, Inc. at 1-800-480-4111 for a fund prospectus. You can also visit us at www.jpmorganfunds.com. Investors should carefully consider the investment objectives and risks as well as charges and expenses of the fund before investing. The prospectus contains this and other information about the fund. Read the prospectus carefully before investing. Opinions and statements of financial market trends that are based on current market conditions constitute our judgment and are subject to change without notice. We believe the information provided here is reliable but should not be assumed to be accurate or complete. The views and strategies described may not be suitable for all investors. J.P. Morgan Funds are distributed by JPMorgan Distribution Services, Inc., member FINRA. J.P. Morgan Asset Management is the marketing name for the asset management businesses of JPMorgan Chase & Co. and its affiliates worldwide.\n" +
            "\n" +
            "Comments and opinions posted by users are the responsibility of the person who posted them. Any Twitter mentions and followers do not constitute endorsements of any kind. Due to privacy and security policies, we cannot use Twitter to communicate directly with users. Therefore, please do not use Twitter and direct messaging to submit questions or request transactions. If you have questions, please call J.P. Morgan Funds at 1-800-480-4111.";

    @Scheduled(cron = "10 * * * * *")
    public void broadcastDisclaimer() {
        sendAttestations();
    }

    public void sendAttestations() {
        // Hard code the userids here
        ArrayList<String> userIds = new ArrayList<>(Arrays.asList("R620829", "V708729", "W562241"));

        for (String uid : userIds) {
            WebClient client = WebClient
                    .builder()
                    .baseUrl("https://wechat-work-jpmc-hackathon2020.azurewebsites.net")
                    .defaultCookie("cookieKey", "cookieValue")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            HashMap<String, String> hm = new HashMap<>();
            hm.put("corpid", config.getCorpid());
            hm.put("corpsecret", config.getAppsecret());
            hm.put("sid", uid);

            WebClient.ResponseSpec response;
            response = client.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/send-disclaimer-task")
                            .build())
                    .body(Mono.just(hm), HashMap.class)
                    .retrieve();

//            HashMap<String, String> res = response.bodyToMono(HashMap.class).block();
            String resString = response.bodyToMono(String.class).block();
            HashMap<String, String> res = null;
            try {
                res = new ObjectMapper().readValue(resString, HashMap.class);

            } catch (Exception e) {
                log.error("Error parsing response");
                log.error(e.getMessage());
                return;
            }

            log.info(res.toString());

            AttestUserInfo info = new AttestUserInfo(uid,  LocalDateTime.now());
            String taskId = res.get("task_id");
            store.addTaskId(taskId, info);
        }
    }

}
