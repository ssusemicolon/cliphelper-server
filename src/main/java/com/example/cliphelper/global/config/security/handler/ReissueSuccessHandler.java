package com.example.cliphelper.global.config.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.cliphelper.domain.user.service.redis.RefreshTokenService;
import com.example.cliphelper.global.config.security.dto.JwtDto;
import com.example.cliphelper.global.config.security.util.JwtUtil;
import com.example.cliphelper.global.result.ResultCode;
import com.example.cliphelper.global.result.ResultResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReissueSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        final JwtDto dto = handleRefreshSuccess(authentication);
        HandlerUtility.writeResponse(request, response, ResultResponse.of(ResultCode.USER_TOKEN_REFRESH_SUCCESS, dto));

    }

    private JwtDto handleRefreshSuccess(Authentication authentication) {
        final JwtDto jwtDto = jwtUtil.createJwt(authentication);
        log.info("save refresh token: {}, {}", authentication.getName(), jwtDto.getRefreshToken());
        refreshTokenService.addRefreshToken(Long.valueOf(authentication.getName()), jwtDto.getRefreshToken());
        return jwtDto;
    }

}
