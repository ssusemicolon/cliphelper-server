package com.example.cliphelper.domain.user.dto;

import com.example.cliphelper.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class UserDetailedProfileResponseDto {
    private final Long userId;
    private final String email;
    private final String username;
    private final String picture;
    private final boolean enableNotifications;
    private final int articleCount;
    private final int collectionCount;
    private final int followerCount;

    public static UserDetailedProfileResponseDto of(User user, int followerCount) {
        return UserDetailedProfileResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .picture(user.getPicture())
                .enableNotifications(user.isEnableNotifications())
                .articleCount(user.getArticles().size())
                .collectionCount(user.getCollections().size())
                .followerCount(followerCount)
                .build();
    }
}
