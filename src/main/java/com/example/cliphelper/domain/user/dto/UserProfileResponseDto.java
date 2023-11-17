package com.example.cliphelper.domain.user.dto;

import com.example.cliphelper.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@RequiredArgsConstructor
public class UserProfileResponseDto {
    private final Long userId;
    private final String email;
    private final String username;
    private final String picture;

    public static UserProfileResponseDto of(User user) {
        return UserProfileResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .picture(user.getPicture())
                .build();
    }
}
