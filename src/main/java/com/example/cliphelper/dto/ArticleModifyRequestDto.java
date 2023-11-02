package com.example.cliphelper.dto;

import com.example.cliphelper.entity.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ArticleModifyRequestDto {
    private String url;
    private String title;
    private String description;
    private String memo;

    private List<String> tags;
}
