package com.example.cliphelper.global.config.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.cliphelper.domain.user.entity.redis.RefreshToken;
import com.example.cliphelper.domain.user.service.redis.RefreshTokenService;
import com.example.cliphelper.global.config.security.exception.JwtInvalidException;
import com.example.cliphelper.global.config.security.exception.LogoutByAnotherException;
import com.example.cliphelper.global.config.security.token.RefreshRequestToken;
import com.example.cliphelper.global.config.security.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class RefreshAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String accessToken = (String) authentication.getPrincipal();
        final String refreshToken = (String) authentication.getCredentials();

        if (accessToken == null || accessToken.equals("") || refreshToken == null || refreshToken.equals("")) {
            throw new JwtInvalidException();
        }

        final Authentication accessAuthentication = jwtUtil.getAuthenticationIgnoreExpiration(accessToken);
        final String memberId = (String) accessAuthentication.getName();

        log.info("memberId in provider: {}", memberId);

        // 사용할 수 있는(저장된) 토큰인지 확인
        final RefreshToken token = refreshTokenService.findRefreshToken(Long.valueOf(memberId), refreshToken)
                .orElseThrow(LogoutByAnotherException::new);

        log.info("refresh token: is valid");

        refreshTokenService.deleteRefreshToken(token);

        return accessAuthentication;

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return RefreshRequestToken.class.isAssignableFrom(aClass);
    }
}
