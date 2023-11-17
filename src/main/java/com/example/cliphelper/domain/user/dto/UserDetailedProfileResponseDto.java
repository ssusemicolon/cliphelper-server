package com.example.cliphelper.domain.user.dto;

import com.example.cliphelper.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class UserDetailedProfileResponseDto extends UserProfileResponseDto {
    private final boolean enableNotifications;
    private final int articleCount;
    private final int collectionCount;
    private final int followerCount;

    public UserDetailedProfileResponseDto(Long userId, String email, String username, String picture,
                                          boolean enableNotifications, int articleCount,
                                          int collectionCount, int followerCount) {
        super(userId, email, username, picture);
        this.enableNotifications = enableNotifications;
        this.articleCount = articleCount;
        this.collectionCount = collectionCount;
        this.followerCount = followerCount;
    }

    public static UserDetailedProfileResponseDto of(User user) {
        return UserDetailedProfileResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .picture(user.getPicture())
                .enableNotifications(user.isEnableNotifications())
                .articleCount(user.getArticles().size())
                .collectionCount(user.getCollections().size())
                .followerCount(user.getFollowerCount())
                .build();
    }
}
