package com.example.cliphelper.global.config.security.vo;

import lombok.Data;

@Data
public class GoogleOAuthResult {
    private String iss;
    private String azp;
    private String aud;
    private String sub;
    private String email;
    private Boolean email_verified;
    private String at_hash;
    private String nonce;
    private String name;
    private String picture;
    private String given_name;
    private String family_name;
    private String local;
    private String iat;
    private String exp;
    private String alg;
    private String kid;
    private String typ;
}
