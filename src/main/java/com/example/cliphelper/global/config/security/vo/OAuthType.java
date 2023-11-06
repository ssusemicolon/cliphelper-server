package com.example.cliphelper.global.config.security.vo;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OAuthType {
    GOOGLE, KAKAO;

    @JsonCreator
    public static OAuthType from(String s) {
        return OAuthType.valueOf(s.toUpperCase());
    }
}
