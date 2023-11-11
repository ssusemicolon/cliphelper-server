package com.example.cliphelper.domain.user.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cliphelper.domain.user.dto.LogoutRequestDto;
import com.example.cliphelper.domain.user.service.redis.RefreshTokenService;
import com.example.cliphelper.global.config.security.util.SecurityUtils;
import com.example.cliphelper.global.result.ResultCode;
import com.example.cliphelper.global.result.ResultResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final SecurityUtils securityUtils;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/auth/logout")
    public ResultResponse logoutUser(@Valid @RequestBody LogoutRequestDto logoutRequest) {
        final Long id = securityUtils.getCurrentUserId();

        try {
            refreshTokenService.deleteRefreshTokenByValue(id, logoutRequest.getRefreshToken());
        } catch (Exception e) {

        }

        return ResultResponse.of(ResultCode.USER_SIGN_OUT_SUCCESS);
    }
}
