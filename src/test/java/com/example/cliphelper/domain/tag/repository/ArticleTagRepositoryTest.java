package com.example.cliphelper.domain.tag.repository;

import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.article.repository.ArticleRepository;
import com.example.cliphelper.domain.tag.entity.ArticleTag;
import com.example.cliphelper.domain.tag.entity.Tag;
import com.example.cliphelper.util.ArticleUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleTagRepositoryTest {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ArticleTagRepository articleTagRepository;

    @Test
    void articleId와_tagId로_ArticleTag_객체_조회() throws Exception {
        // given
        Article article = ArticleUtils.newInstance();
        Tag tag = new Tag("JUnit");
        ArticleTag articleTag = new ArticleTag(article, tag);

        articleRepository.save(article);
        tagRepository.save(tag);
        articleTagRepository.save(articleTag);

        // when
        ArticleTag result = articleTagRepository.findByArticleIdAndTagId(article.getId(), tag.getId())
                .orElse(null);

        // then
        assertThat(result).isEqualTo(articleTag);
    }

    @Test
    @DisplayName("articleId를 통해, 해당 articleId를 가진 아티클의 태그 리스트를 조회할 수 있다.")
    void 특정_articleId를_가진_ArticleTag_객체_리스트_조회() throws Exception {
        // given
        Article article = ArticleUtils.newInstance();
        List<Tag> tagList = new ArrayList<>();
        List<ArticleTag> articleTagList = new ArrayList<>();

        while (tagList.size() < 3) {
            Tag tag = new Tag(RandomStringUtils.random(5, true, false));
            ArticleTag articleTag = new ArticleTag(article, tag);

            tagList.add(tag);
            articleTagList.add(articleTag);
        }

        articleRepository.save(article);
        tagRepository.saveAll(tagList);
        articleTagRepository.saveAll(articleTagList);

        // when
        List<ArticleTag> result = articleTagRepository.findByArticleId(article.getId());

        // then
        assertThat(result).containsExactlyElementsOf(articleTagList);
    }

}