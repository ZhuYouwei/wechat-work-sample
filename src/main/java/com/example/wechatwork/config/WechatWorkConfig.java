package com.example.wechatwork.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
@lombok.Value
public class WechatWorkConfig {
    @Value( "${wechatwork.corpid}" )
    private String corpid;

    @Value( "${wechatwork.corpsecret}" )
    private String corpsecret;

    @Value( "${wechatwork.appsercret}" )
    private String appsecret;

    @Value( "${wechatwork.externalcontact.token}" )
    private String externalContactToken;

    @Value( "${wechatwork.externalcontact.aesKey}" )
    private String externalContactAesKey;

}