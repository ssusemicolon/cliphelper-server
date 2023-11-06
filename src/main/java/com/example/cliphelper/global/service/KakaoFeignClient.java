package com.example.cliphelper.global.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.cliphelper.global.config.security.vo.KakaoOAuthResult;

@FeignClient(name = "KakaoFeignClient", url = "https://kapi.kakao.com/v2/user/me")
public interface KakaoFeignClient {

    @GetMapping("")
    KakaoOAuthResult verify(@RequestHeader("Authorization") String token);
}
