package com.example.cliphelper.global.config.security.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.cliphelper.global.config.security.vo.OAuthType;

import lombok.Data;

@Data
public class LoginRequestDto {

    @NotNull(message = "type은 필수입력입니다.")
    OAuthType type;

    @NotBlank(message = "key는 필수입력입니다.")
    String key;
}
