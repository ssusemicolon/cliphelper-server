package com.example.cliphelper.domain.article.dto;

import com.example.cliphelper.domain.article.entity.Article;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@RequiredArgsConstructor
public class ArticleResponseDto {
    private final Long articleId;
    private final String url;
    private final String fileUrl;
    private final String thumbnail;
    private final String title;
    private final String description;
    private final String memo;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss+09:00")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss+09:00")
    private final LocalDateTime recentAccessTime;
    private final List<String> tags;
    private final Long userId;

    public static ArticleResponseDto of(Article article) {
        return ArticleResponseDto.builder()
                .articleId(article.getId())
                .url(article.getUrl())
                .fileUrl(article.getFileUrl())
                .thumbnail(article.getThumbnail())
                .title(article.getTitle())
                .description(article.getDescription())
                .memo(article.getMemo())
                .createdAt(article.getCreatedAt())
                .recentAccessTime(article.getRecentAccessTime())
                .tags(article.getTags())
                .userId(article.getUser().getId())
                .build();
    }

    public static List<ArticleResponseDto> ofList(List<Article> articles) {
        return articles.stream()
                .map(article -> ArticleResponseDto.of(article))
                .collect(Collectors.toList());
    }
}
