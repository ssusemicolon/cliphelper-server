package com.example.cliphelper.domain.article.dto;

import com.example.cliphelper.domain.article.entity.Article;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleRequestDto {
    @NotBlank
    private String url;

    @NotBlank
    private String title;

    private String description;

    private String memo;

    private List<String> tags;

    public Article toEntity() {
        LocalDateTime now = LocalDateTime.now();
        return new Article(
                this.title,
                this.url,
                this.description,
                now,
                now
        );
    }
}