package com.example.cliphelper.repository;

import com.example.cliphelper.entity.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
    Optional<ArticleTag> findByArticleIdAndTagId(Long articleId, Long tagId);
}
