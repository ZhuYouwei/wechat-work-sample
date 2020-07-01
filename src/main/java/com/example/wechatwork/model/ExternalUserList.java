package com.example.wechatwork.model;

import lombok.Data;

import java.util.List;

@Data
public class ExternalUserList {
    private String errcode;
    private String errmsg;
    private List<String> external_userid;
}
