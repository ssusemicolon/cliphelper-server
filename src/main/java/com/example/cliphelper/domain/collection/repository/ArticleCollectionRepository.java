package com.example.cliphelper.domain.collection.repository;

import com.example.cliphelper.domain.collection.entity.ArticleCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleCollectionRepository extends JpaRepository<ArticleCollection, Long> {
    Optional<ArticleCollection> findByArticleIdAndCollectionId(Long articleId, Long collectionId);
    List<ArticleCollection> findByArticleId(Long articleId);

}
