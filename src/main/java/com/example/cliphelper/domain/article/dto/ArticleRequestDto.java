package com.example.cliphelper.domain.article.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.cliphelper.domain.article.entity.Article;

import lombok.Data;

@Data
public class ArticleRequestDto {
    private String url;

    private String thumbnail;

    private MultipartFile file;

    private String title;

    private String description;

    private String memo;

    private List<String> tags;

    public Article toEntity() {
        LocalDateTime now = LocalDateTime.now();
        return new Article(
                this.url,
                this.thumbnail,
                this.title,
                this.description,
                this.memo,
                now,
                now);
    }
}
