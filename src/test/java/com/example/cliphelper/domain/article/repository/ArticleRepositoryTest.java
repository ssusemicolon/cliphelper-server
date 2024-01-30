package com.example.cliphelper.domain.article.repository;

import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.user.repository.UserRepository;
import com.example.cliphelper.global.error.ErrorCode;
import com.example.cliphelper.global.error.exception.EntityNotFoundException;
import com.example.cliphelper.util.ArticleUtils;
import com.example.cliphelper.util.UserUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ArticleRepository articleRepository;

    @Test
    @DisplayName("findByUserIdOrderByCreatedAtDesc() 호출 시, createdAt 필드를 기준으로 내림차순으로 Article 리스트를 리턴한다.")
    void 특정_회원의_아티클_목록을_생성날짜_기준으로_내림차순으로_조회() {
        // given
        User user = UserUtils.newInstance();
        List<Article> articleList = ArticleUtils.newInstanceList(user, 5);

        userRepository.save(user);
        articleRepository.saveAll(articleList);

        // when
        List<Article> result = articleRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        // then
        for (int i = 0; i < result.size() - 1; i++) {
            Article precedingArticle = result.get(i);
            Article followingArticle = result.get(i + 1);

            assertThat(precedingArticle.getCreatedAt()).isAfterOrEqualTo(followingArticle.getCreatedAt());
        }
    }

    @Test
    @DisplayName("findTopByUserIdOrderByRecentAccessTimeAsc() 호출 시 recentAccessTime 필드가 가장 옛날 날짜인 Article을 리턴한다.")
    void 특정_회원의_아티클_목록_중_열어본지가_가장_오래_된_아티클_조회() {
        // given
        User user = UserUtils.newInstance();
        List<Article> articleList = ArticleUtils.newInstanceList(user, 3);

        userRepository.save(user);
        articleRepository.saveAll(articleList);

        // when
        Article result = articleRepository.findTopByUserIdOrderByRecentAccessTimeAsc(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND));

        // then
        for (Article article : articleList) {
            assertThat(result.getRecentAccessTime()).isBeforeOrEqualTo(article.getRecentAccessTime());
        }
    }

    @Test
    @DisplayName("countByUserId() 호출 시 파라미터로 전달 받은 userId를 가진 회원의 아티클 개수를 리턴한다.")
    void 특정_회원의_아티클_목록의_개수를_조회() {
        // given
        List<User> userList = UserUtils.newInstanceList(3);
        List<Article> articleListOfUserA = ArticleUtils.newInstanceList(userList.get(0), 7);
        List<Article> articleListOfUserB = ArticleUtils.newInstanceList(userList.get(1), 2);
        List<Article> articleListOfUserC = ArticleUtils.newInstanceList(userList.get(2), 0);

        userRepository.saveAll(userList);
        articleRepository.saveAll(articleListOfUserA);
        articleRepository.saveAll(articleListOfUserB);
        articleRepository.saveAll(articleListOfUserC);

        // when
        Long articleCountOfUserA = articleRepository.countByUserId(userList.get(0).getId());
        Long articleCountOfUserB = articleRepository.countByUserId(userList.get(1).getId());
        Long articleCountOfUserC = articleRepository.countByUserId(userList.get(2).getId());

        // then
        assertThat(articleCountOfUserA).isEqualTo(articleListOfUserA.size());
        assertThat(articleCountOfUserB).isEqualTo(articleListOfUserB.size());
        assertThat(articleCountOfUserC).isEqualTo(articleListOfUserC.size());
    }
}