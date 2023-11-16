package com.example.cliphelper.domain.article.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.cliphelper.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import com.example.cliphelper.domain.article.entity.Article;

import lombok.Data;

@Data
public class ArticleRequestDto {
    private String url;

    private MultipartFile file;

    private String thumbnail;

    private String title;

    private String description;

    private String memo;

    private List<String> tags;

    // 일반 아티클인 경우
    public Article toEntity(User user) {
        return toEntity(null, null, user);
    }

    // 파일 아티클인 경우
    public Article toEntity(String fileUrl, String uuid, User user) {
        LocalDateTime now = LocalDateTime.now();
        return Article.builder()
                .id(null)
                // 파일 아티클인 경우 null이 들어갈 건데,
                // null을 직접 전달해야 하나, null로 저장되어 있을 url을 전달해야 하나?
                .url(url)
                .fileUrl(fileUrl)
                .uuid(uuid)
                .thumbnail(thumbnail)
                .title(title)
                .description(description)
                .memo(memo)
                .createdAt(now)
                .recentAccessTime(now)
                .user(user)
                .build();
    }
}
