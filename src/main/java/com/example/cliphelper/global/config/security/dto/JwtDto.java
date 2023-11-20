package com.example.cliphelper.global.config.security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtDto {
    private String type;
    private String accessToken;
    private String refreshToken;
    private Long userId;
}
