package com.example.cliphelper.domain.user.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class LogoutRequestDto {
    @NotEmpty()
    private String refreshToken;
}
