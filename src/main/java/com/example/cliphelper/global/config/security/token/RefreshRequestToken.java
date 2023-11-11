package com.example.cliphelper.global.config.security.token;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class RefreshRequestToken extends UsernamePasswordAuthenticationToken {
    public RefreshRequestToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public RefreshRequestToken(Object principal, Object credentials,
            Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static RefreshRequestToken of(String accessToken, String refreshToken) {
        return new RefreshRequestToken(accessToken, refreshToken);
    }
}
