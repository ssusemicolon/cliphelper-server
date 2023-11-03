package com.example.cliphelper.domain.bookmark.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkRequestDto {
    @NotNull
    private Long userId;
    @NotNull
    private Long collectionId;
}
