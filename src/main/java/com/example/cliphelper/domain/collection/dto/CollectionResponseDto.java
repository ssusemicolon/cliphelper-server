package com.example.cliphelper.domain.collection.dto;

import com.example.cliphelper.domain.article.dto.ArticleResponseDto;
import com.example.cliphelper.domain.collection.entity.Collection;
import com.example.cliphelper.domain.user.dto.UserProfileResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
@RequiredArgsConstructor
public class CollectionResponseDto {
    private final Long collectionId;
    private final String title;
    private final String description;
    private final Boolean isPublic;
    private final List<ArticleResponseDto> articles;
    private final int articleCount;
    private final UserProfileResponseDto user;
    private Boolean isBookmarked;

    public static CollectionResponseDto of(Collection collection) {
        return of(collection, false);
    }

    public static CollectionResponseDto of (Collection collection, boolean isBookmarked) {
        return CollectionResponseDto.builder()
                .collectionId(collection.getId())
                .title(collection.getTitle())
                .description(collection.getDescription())
                .isPublic(collection.isPublic())
                .articles(ArticleResponseDto.ofList(collection.getArticles()))
                .articleCount(collection.getArticleCount())
                .user(UserProfileResponseDto.of(collection.getUser()))
                .isBookmarked(isBookmarked)
                .build();
    }
}
