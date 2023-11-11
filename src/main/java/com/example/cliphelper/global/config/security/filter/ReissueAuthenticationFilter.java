package com.example.cliphelper.global.config.security.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.cliphelper.global.config.security.dto.RefreshRequestDto;
import com.example.cliphelper.global.config.security.token.RefreshRequestToken;
import com.example.cliphelper.global.config.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReissueAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtUtil jwtUtil;
    private static final AntPathRequestMatcher ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/auth/reissue",
            "POST");
    final ObjectMapper objectMapper = new ObjectMapper();

    public ReissueAuthenticationFilter(JwtUtil jwtUtil) {
        super(ANT_PATH_REQUEST_MATCHER);
        this.jwtUtil = jwtUtil;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        log.info("재발급 필터!");
        try {
            final String accessToken = jwtUtil.extractJwt(request);
            log.info("access token: {}", accessToken);

            final String refreshToken = this.parseRefreshToken(request);
            log.info("refresh token: {}", refreshToken);

            final RefreshRequestToken authRequest = RefreshRequestToken.of(accessToken, refreshToken);

            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException ioe) {
            throw new AuthenticationServiceException("Authentication failed while converting request body.");
        }
    }

    private String parseRefreshToken(HttpServletRequest request) throws IOException {
        final String requestBody = IOUtils.toString(request.getReader());
        final RefreshRequestDto refreshRequest = objectMapper.readValue(requestBody, RefreshRequestDto.class);
        final String refreshToken = refreshRequest.getRefreshToken();

        return refreshToken;
    }

}