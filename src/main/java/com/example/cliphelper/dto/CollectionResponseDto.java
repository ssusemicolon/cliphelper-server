package com.example.cliphelper.dto;

import com.example.cliphelper.entity.Collection;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder
@Getter
@RequiredArgsConstructor
public class CollectionResponseDto {
    private final Long collectionId;
    private final String title;
    private final String description;
    private final boolean isPublic;
    private final Long userId;
    private final List<Long> articles;
    private final int articleCount;

    public static CollectionResponseDto of(Collection collection, List<Long> articles) {
        return CollectionResponseDto.builder()
                .collectionId(collection.getId())
                .title(collection.getTitle())
                .description(collection.getDescription())
                .isPublic(collection.isPublic())
                .userId(collection.getUser().getId())
                .articles(articles)
                .articleCount(collection.getArticleCount())
                .build();
    }
}
