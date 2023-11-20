package com.example.cliphelper.global.config.security.token;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.ToString;

@ToString
public class OAuthLoginToken extends UsernamePasswordAuthenticationToken {
    public OAuthLoginToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public OAuthLoginToken(Object principal, Object credentials,
            Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static OAuthLoginToken of(String token) {
        return new OAuthLoginToken(token, token);
    }
}