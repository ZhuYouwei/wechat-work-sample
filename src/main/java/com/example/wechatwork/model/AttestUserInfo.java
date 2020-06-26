package com.example.wechatwork.model;

import java.time.LocalDateTime;

public class AttestUserInfo {
    private String userId;
    private LocalDateTime attestStartDatetime;
    private LocalDateTime attestEndDatetime;
    private Boolean attested;

    public LocalDateTime getAttestEndDatetime() {
        return attestEndDatetime;
    }

    public void setAttestEndDatetime(LocalDateTime attestEndDatetime) {
        this.attestEndDatetime = attestEndDatetime;
    }

    public Boolean getAttested() {
        return attested;
    }

    public void setAttested(Boolean attested) {
        this.attested = attested;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getAttestStartDatetime() {
        return attestStartDatetime;
    }

    public void setAttestStartDatetime(LocalDateTime attestStartDatetime) {
        this.attestStartDatetime = attestStartDatetime;
    }

    public AttestUserInfo(String userId, LocalDateTime attestStartDatetime) {
        this.userId = userId;
        this.attestStartDatetime = attestStartDatetime;
    }
}
