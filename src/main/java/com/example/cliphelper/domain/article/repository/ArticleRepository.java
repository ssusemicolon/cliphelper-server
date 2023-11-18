package com.example.cliphelper.domain.article.repository;

import com.example.cliphelper.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByUserId(Long userId);

    List<Article> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 파라미터로 전달받은 userId를 가진 회원의 아티클 중, 가장 접근한지 오래된 아티클을 1개 조회하는 함수
    Optional<Article> findTopByUserIdOrderByRecentAccessTimeAsc(Long userId);

    Long countByUserId(Long userId);
}