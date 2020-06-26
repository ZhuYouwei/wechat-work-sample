package com.example.wechatwork.gateway;

import com.example.wechatwork.config.WechatWorkConfig;
import com.example.wechatwork.domain.ExternalClientResult;
import com.example.wechatwork.model.GetTokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@Component
@Slf4j
public class WechatWorkGateway {
    @Autowired
    private WechatWorkConfig config;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new Jdk8Module());

    public GetTokenResponse getAccessToken() {
        WebClient client = WebClient
                .builder()
                .baseUrl("https://qyapi.weixin.qq.com/")
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();


        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/cgi-bin/gettoken")
                        .queryParam("corpid", config.getCorpid())
                        .queryParam("corpsecret", config.getCorpsecret())
                        .build())
                .retrieve()
                .bodyToMono(GetTokenResponse.class)
                .block();
    }

    public GetTokenResponse getAccessTokenForUserApp() {
        WebClient client = WebClient
                .builder()
                .baseUrl("https://qyapi.weixin.qq.com/")
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/cgi-bin/gettoken")
                        .queryParam("corpid", config.getCorpid())
                        .queryParam("corpsecret", config.getAppsecret())
                        .build())
                .retrieve()
                .bodyToMono(GetTokenResponse.class)
                .block();
    }

    public String getCallbackIp(String accessToken) {
        WebClient client = WebClient
                .builder()
                .baseUrl("https://qyapi.weixin.qq.com/")
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        WebClient.ResponseSpec response;
        response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/cgi-bin/getcallbackip")
                        .queryParam("access_token", accessToken)
                        .build())
                .retrieve();

        return response.bodyToMono(String.class).block();
    }


    public String sendAppMessage(String accessToken, String messageContent, String targetUser) {

        WebClient client = WebClient
                .builder()
                .baseUrl("https://qyapi.weixin.qq.com/")
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        val body = new HashMap<String, Object>();
        body.put("touser", targetUser);
        body.put("msgtype", "text");
        body.put("agentid", "1000011");
        val content = new HashMap<String, String>();
        content.put("content", messageContent);
        body.put("text", content);

        WebClient.ResponseSpec response;
        response = client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/cgi-bin/message/send")
                        .queryParam("access_token", accessToken)
                        .build())
                .body(Mono.just(body), HashMap.class)
                .retrieve();


        log.info("Message has been sent to internal users {}", targetUser);

        return response.bodyToMono(String.class).block();
    }

    public BigDecimal fetchExternalContactCount(String accessToken, String userId) {
        try {
            WebClient client = WebClient
                    .builder()
                    .baseUrl("https://qyapi.weixin.qq.com/")
                    .defaultCookie("cookieKey", "cookieValue")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            WebClient.ResponseSpec response;
            response = client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/cgi-bin/externalcontact/list")
                            .queryParam("access_token", accessToken)
                            .queryParam("userid", userId)
                            .build())
                    .retrieve();

            String json = response.bodyToMono(String.class).block();
            log.info("JSON1 {}", json);

            ExternalClientResult result = objectMapper.readValue(json, ExternalClientResult.class);

            List<String> username = new ArrayList<>();

            result.getExternalUserList().forEach(externalContact->{
               String name = this.fetchExternalUserDetail(accessToken,externalContact);
               username.add(name);
            });

            ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);

            log.info("Current external user list {}", username);
//            
//            String x = node.get("external_userid").toString();
//            int cnt = StringUtils.countOccurrencesOf(x, ",") + 1;
//
//            if (x.length() < 5) {
//                log.info("Client count {}", x);
//                return BigDecimal.ZERO;
//            }
//
//            log.info("Client count {}", cnt);

            return BigDecimal.valueOf(result.getExternalUserList().size());

//            int clientCount = ((String[]) node.get("external_userid")).size();

//
//            return BigDecimal.valueOf(clientCount);
        } catch (Exception e) {
            log.error("unable to parse external client information");
            return BigDecimal.ZERO;
        }
    }

    public String fetchExternalUserDetail(String accessToken, String externalUserId) {
        try {
            WebClient client = WebClient
                    .builder()
                    .baseUrl("https://qyapi.weixin.qq.com/")
                    .defaultCookie("cookieKey", "cookieValue")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();


            WebClient.ResponseSpec response;
            response = client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/cgi-bin/externalcontact/get")
                            .queryParam("access_token", accessToken)
                            .queryParam("external_userid", externalUserId)
                            .build())
                    .retrieve();

            String json = response.bodyToMono(String.class).block();


            ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);

            String clientName = node.get("external_contact").get("name").toString();

            return clientName;

        } catch (Exception e) {
            log.error("unable to parse external client information");
            return "valuable client";
        }

    }

    public String sendMessageTask(String accessToken, String externalUserId, String text) {
        WebClient client = WebClient
                .builder()
                .baseUrl("https://qyapi.weixin.qq.com/")
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        val body = new HashMap<String, Object>();
        body.put("chat_type", "single");

        val content = new HashMap<String, String>();
        content.put("content", text);
        body.put("text", content);

        val externalUserIds = Arrays.asList(externalUserId);
        body.put("external_userid", externalUserIds);

        WebClient.ResponseSpec response;
        response = client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/cgi-bin/externalcontact/add_msg_template")
                        .queryParam("access_token", accessToken)
                        .build())
                .body(Mono.just(body), HashMap.class)
                .retrieve();

        log.info("Message has been sent to external users {}", externalUserIds);

        return response.bodyToMono(String.class).block();
    }

    public String sendWelcome(String accessToken, String welcomeCode, String text) {
        WebClient client = WebClient
                .builder()
                .baseUrl("https://qyapi.weixin.qq.com/")
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        val body = new HashMap<String, Object>();
        body.put("welcome_code", welcomeCode);

        val content = new HashMap<String, String>();
        content.put("content", text);
        body.put("text", content);

        WebClient.ResponseSpec response;
        response = client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/cgi-bin/externalcontact/send_welcome_msg")
                        .queryParam("access_token", accessToken)
                        .build())
                .body(Mono.just(body), HashMap.class)
                .retrieve();

        log.info("Welcome message has been sent to the new users");

        return response.bodyToMono(String.class).block();
    }

    public String send(String accessToken, String touser, String agentid, String message) {
        WebClient client = WebClient
                .builder()
                .baseUrl("https://qyapi.weixin.qq.com/")
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        val body = new HashMap<String, Object>();
        body.put("ToUser", touser);
        body.put("MsgType", "text");
        body.put("AgentID", agentid);
        val content = new HashMap<String, String>();
        content.put("Content", message);
        body.put("text", content);

        WebClient.ResponseSpec response;
        response = client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/cgi-bin/message/send")
                        .queryParam("access_token", accessToken)
                        .build(body))
                .retrieve();

        return response.bodyToMono(String.class).block();
    }
}
