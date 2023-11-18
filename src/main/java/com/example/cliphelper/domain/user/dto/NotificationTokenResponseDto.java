package com.example.cliphelper.domain.user.dto;

import com.example.cliphelper.domain.user.entity.NotificationToken;
import com.example.cliphelper.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Builder
@Getter
@RequiredArgsConstructor
public class NotificationTokenResponseDto {
    private final Long notificationTokenId;
    private final String deviceToken;
    private final UserProfileResponseDto user;

    public static NotificationTokenResponseDto of(NotificationToken notificationToken) {
        return NotificationTokenResponseDto.builder()
                .notificationTokenId(notificationToken.getId())
                .deviceToken(notificationToken.getDeviceToken())
                .user(UserProfileResponseDto.of(notificationToken.getUser()))
                .build();
    }
}
