package com.example.cliphelper.domain.article.dto;

import com.example.cliphelper.domain.article.entity.Article;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class ArticleResponseDto {
    private final Long articleId;
    private final String url;
    private final String title;
    private final String description;
    private final String memo;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss+09:00")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss+09:00")
    private final LocalDateTime recentAccessTime;

    // List<Tag> 형태로 쓸 경우, 전송 형태가 [ { id: 1, name: 스프링 }, { }, .. ]과 같음
    // 클라이언트에게 전달해줄 값은 name만이므로, List<String> 타입으로 선언함
    private final List<String> tags;

    private final Long userId;

    public static ArticleResponseDto of(Article article, List<String> tags) {
        return ArticleResponseDto.builder()
                .articleId(article.getId())
                .url(article.getUrl())
                .title(article.getTitle())
                .description(article.getDescription())
                .memo(article.getMemo())
                .createdAt(article.getCreatedAt())
                .recentAccessTime(article.getRecentAccessTime())
                .tags(tags)
                .userId(article.getUser().getId())
                .build();
    }
}
