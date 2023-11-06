package com.example.cliphelper.global.config.security.vo;

import lombok.Data;

@Data
public class KakaoOAuthResult {
    private Long id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Data
    public static class Properties {
        String nickname;
        String profile_image;
        String thumbnail_image;
    }

    @Data
    public static class KakaoAccount {
        Boolean has_email;
        Boolean email_needs_agreement;
        Boolean is_email_valid;
        Boolean is_email_verified;
        String email;

    }
}
