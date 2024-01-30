package com.example.cliphelper.util;

import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.user.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArticleUtils {
    private static final String MOCK_URL = "https://velog.io/@bflykky/테스트-코드의-필요성과-관련-개념-정리";

    // User 필드 null로 생성
    public static Article newInstance() {
        return newInstance(null);
    }

    // User 필드 파라미터로 전달받아 생성
    public static Article newInstance(User user) {
        LocalDateTime createdAt = LocalDateTime.now().minusDays(10L);
        LocalDateTime recentAccessTime = createdAt.plusDays(RandomUtils.nextLong(1, 10));

        String title = RandomStringUtils.random(5, true, false);
        return Article.builder()
                .id(null)
                .url(MOCK_URL)
                .fileUrl(null)
                .uuid(null)
                .thumbnail(null)
                .title(title)
                .description(null)
                .memo(null)
                .createdAt(createdAt)
                .recentAccessTime(recentAccessTime)
                .user(user)
                .build();
    }

    public static List<Article> newInstanceList(User user, int articleCount) {
        List<Article> articleList = new ArrayList<>();
        Set<String> titleSet = new HashSet<>();

        while (articleList.size() < articleCount) {
            final Article article = newInstance(user);
            if (titleSet.contains(article.getTitle())) {
                continue;
            }

            articleList.add(article);
            titleSet.add(article.getTitle());
        }

        return articleList;
    }
}
