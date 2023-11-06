package com.example.cliphelper.global.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.cliphelper.global.config.security.vo.GoogleOAuthResult;

@FeignClient(name = "GoogleFeignClient", url = "https://www.googleapis.com/oauth2/v3/tokeninfo")
public interface GoogleFeignClient {

    @GetMapping("/")
    GoogleOAuthResult verify(@RequestParam("id_token") String key);
}
