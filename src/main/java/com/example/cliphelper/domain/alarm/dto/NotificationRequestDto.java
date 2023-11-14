package com.example.cliphelper.domain.alarm.dto;

import com.example.cliphelper.domain.article.dto.ArticleResponseDto;
import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.user.entity.User;
import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDto {
    private static final String NOTIFICATION_MESSAGE = "잠깐! 여유 시간이 생긴 %s님께 위 아티클을 추천해 드려요.";
    private String deviceToken;
    private Long articleId;
    private String title;
    private String body;
    private String image;

    public Notification toNotification() {
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .setImage(image)
                .build();
    }

    public static NotificationRequestDto of(String deviceToken, ArticleResponseDto articleResponseDto, User user) {
        return NotificationRequestDto.builder()
                .deviceToken(deviceToken)
                .articleId(articleResponseDto.getArticleId())
                .title(articleResponseDto.getTitle())
                .body(String.format(NOTIFICATION_MESSAGE, user.getUsername()))
                .image(articleResponseDto.getThumbnail())
                .build();
    }
}
