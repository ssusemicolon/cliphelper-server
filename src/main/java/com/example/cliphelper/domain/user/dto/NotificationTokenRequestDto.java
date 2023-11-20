package com.example.cliphelper.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class NotificationTokenRequestDto {
    @NotBlank
    private String deviceToken;

}
