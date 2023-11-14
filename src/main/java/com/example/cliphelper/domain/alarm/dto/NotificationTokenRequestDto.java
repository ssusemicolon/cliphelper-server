package com.example.cliphelper.domain.alarm.dto;

import javax.validation.constraints.NotBlank;

import com.example.cliphelper.domain.alarm.entity.NotificationToken;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationTokenRequestDto {
    @NotBlank
    private String deviceToken;

    private String mm;

    public NotificationToken toEntity() {
        return null;
    }
}
