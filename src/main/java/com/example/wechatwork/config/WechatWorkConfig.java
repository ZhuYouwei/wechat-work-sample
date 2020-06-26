package com.example.wechatwork.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class WechatWorkConfig {
    @Value( "${wechatwork.corpid}" )
    private String corpid;

    @Value( "${wechatwork.corpsecret}" )
    private String corpsecret;

    @Value( "${wechatwork.appsecret}" )
    private String appsecret;

    @Value( "${wechatwork.externalcontact.token}" )
    private String externalContactToken;

    @Value( "${wechatwork.externalcontact.aesKey}" )
    private String externalContactAesKey;

    public String getCorpid() {
        return corpid;
    }

    public String getCorpsecret() {
        return corpsecret;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public String getExternalContactToken() {
        return externalContactToken;
    }

    public String getExternalContactAesKey() {
        return externalContactAesKey;
    }
}