package com.example.cliphelper.domain.alarm.dto;

import com.example.cliphelper.domain.alarm.entity.NotificationToken;
import com.example.cliphelper.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

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
