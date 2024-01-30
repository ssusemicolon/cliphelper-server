package com.example.cliphelper.domain.collection.repository;

import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.article.repository.ArticleRepository;
import com.example.cliphelper.domain.collection.entity.ArticleCollection;
import com.example.cliphelper.domain.collection.entity.Collection;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.user.repository.UserRepository;
import com.example.cliphelper.util.ArticleUtils;
import com.example.cliphelper.util.CollectionUtils;
import com.example.cliphelper.util.UserUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleCollectionRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ArticleCollectionRepository articleCollectionRepository;
    @Autowired
    CollectionRepository collectionRepository;

    @Test
    void 특정_articleId와_collectionId를_가진_articleCollection_객체_조회() throws Exception {
        // given
        Article article = ArticleUtils.newInstance();
        Collection collection = CollectionUtils.newInstance();
        ArticleCollection articleCollection = new ArticleCollection(article, collection);

        articleRepository.save(article);
        collectionRepository.save(collection);
        articleCollectionRepository.save(articleCollection);

        // when
        ArticleCollection result = articleCollectionRepository.findByArticleIdAndCollectionId(article.getId(), collection.getId())
                .orElse(null);

        // then
        assertThat(result).isEqualTo(articleCollection);
    }

    @Test
    void 특정_articleId를_가진_articleCollection_객체_리스트를_조회() {
        // given
        User user = UserUtils.newInstance();
        Article article = ArticleUtils.newInstance();
        List<Collection> collectionList = CollectionUtils.newInstanceList(5);
        List<ArticleCollection> articleCollectionList = new ArrayList<>();
        for (Collection collection : collectionList) {
            ArticleCollection articleCollection = new ArticleCollection(article, collection);
            articleCollectionList.add(articleCollection);
        }

        userRepository.save(user);
        articleRepository.save(article);
        collectionRepository.saveAll(collectionList);
        articleCollectionRepository.saveAll(articleCollectionList);

        // when
        List<ArticleCollection> result = articleCollectionRepository.findByArticleId(article.getId());

        // then
        assertThat(result.size()).isEqualTo(articleCollectionList.size());
        for (ArticleCollection ac : articleCollectionList) {
            assertThat(result).contains(ac);
        }
    }
}