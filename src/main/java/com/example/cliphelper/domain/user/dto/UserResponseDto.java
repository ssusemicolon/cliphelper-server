package com.example.cliphelper.domain.user.dto;


import com.example.cliphelper.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class UserResponseDto {
    private final Long userId;
    private final String email;
    private final String username;

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }
}
