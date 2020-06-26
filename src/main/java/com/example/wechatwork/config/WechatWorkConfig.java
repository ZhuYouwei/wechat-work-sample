package com.example.wechatwork.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
//@lombok.Value
@Getter
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



}