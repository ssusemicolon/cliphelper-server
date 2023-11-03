package com.example.cliphelper.domain.collection.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CollectionModifyRequestDto {
    private String title;
    private String description;
    private Boolean isPublic;
}
