package com.example.cliphelper.domain.article.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ArticleModifyRequestDto {
    private String url;
    private String thumbnail;
    private String title;
    private String description;
    private String memo;
    private List<String> tags;
}
