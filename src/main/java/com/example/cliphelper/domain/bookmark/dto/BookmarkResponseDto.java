package com.example.cliphelper.domain.bookmark.dto;

import com.example.cliphelper.domain.collection.dto.CollectionResponseDto;
import com.example.cliphelper.domain.collection.entity.Collection;
import com.example.cliphelper.domain.user.entity.User;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class BookmarkResponseDto extends CollectionResponseDto {
    private final User user;

    public static BookmarkResponseDto of(Collection collection, List<Long> articles) {
        return BookmarkResponseDto.builder()
                .collectionId(collection.getId())
                .title(collection.getTitle())
                .description(collection.getDescription())
                .isPublic(collection.isPublic())
                .articles(articles)
                .articleCount(collection.getArticleCount())
                .userId(collection.getUser().getId())
                .user(collection.getUser())
                .build();
    }
}
