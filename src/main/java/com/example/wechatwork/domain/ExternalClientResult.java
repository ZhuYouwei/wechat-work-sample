package com.example.wechatwork.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ExternalClientResult {


    private final int errorCode;


    private final String errmsg;

    @JsonProperty("external_userid")
    private final List<String> externalUserList;

    @JsonCreator
    public ExternalClientResult(@JsonProperty("errcode") int errorCode,
                                @JsonProperty("errmsg") String errmsg,
                                @JsonProperty("external_userid") List<String> externalUserList){

        this.errorCode = errorCode;
        this.errmsg = errmsg;
        this.externalUserList = externalUserList;

    }

}
