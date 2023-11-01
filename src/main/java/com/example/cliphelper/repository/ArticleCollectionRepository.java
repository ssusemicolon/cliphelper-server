package com.example.cliphelper.repository;

import com.example.cliphelper.entity.ArticleCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCollectionRepository extends JpaRepository<ArticleCollection, Long> {
}
