package com.example.cliphelper.domain.collection.dto;

import com.example.cliphelper.domain.collection.entity.Collection;
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
    private final boolean isPublic;
    private final List<Long> articles;
    private final int articleCount;
    private final Long userId;

    public static CollectionResponseDto of(Collection collection, List<Long> articles) {
        return CollectionResponseDto.builder()
                .collectionId(collection.getId())
                .title(collection.getTitle())
                .description(collection.getDescription())
                .isPublic(collection.isPublic())
                .articles(articles)
                .articleCount(collection.getArticleCount())
                .userId(collection.getUser().getId())
                .build();
    }
}