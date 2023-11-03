package com.example.cliphelper.domain.article.service;

import com.example.cliphelper.domain.article.dto.ArticleModifyRequestDto;
import com.example.cliphelper.domain.article.dto.ArticleRequestDto;
import com.example.cliphelper.domain.article.dto.ArticleResponseDto;
import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.collection.entity.ArticleCollection;
import com.example.cliphelper.domain.tag.entity.ArticleTag;
import com.example.cliphelper.domain.collection.entity.Collection;
import com.example.cliphelper.domain.tag.entity.Tag;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.collection.repository.ArticleCollectionRepository;
import com.example.cliphelper.domain.article.repository.ArticleRepository;
import com.example.cliphelper.domain.tag.repository.ArticleTagRepository;
import com.example.cliphelper.domain.collection.repository.CollectionRepository;
import com.example.cliphelper.domain.tag.repository.TagRepository;
import com.example.cliphelper.domain.user.repository.UserRepository;
import com.example.cliphelper.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleTagRepository articleTagRepository;
    private final ArticleCollectionRepository articleCollectionRepository;
    private final CollectionRepository collectionRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final TagService tagService;


    public void createArticle(ArticleRequestDto articleRequestDto) {
        // 여기서 해당 userId를 가진 회원이 존재하는지 여부를 확인하지 않는다.
        // 왜냐면, JWT로 전달받았을 때, 이미 해당 토큰이 유효한지 검증한 이후이기 때문이다.
        Article article = articleRequestDto.toEntity();
        User user = userRepository.findById(articleRequestDto.getUserId())
                .orElse(null);
        article.setUser(user);
        articleRepository.save(article);

        // Tag 엔티티와 ArticleTag 엔티티 save
        tagService.registerTagInArticle(article, articleRequestDto.getTags());
    }

    public List<ArticleResponseDto> findAllArticles() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleResponseDto> articleResponseDtos = new ArrayList<>();

        articles.forEach(article -> {
            List<ArticleTag> articleTags = article.getArticleTags();
            List<String> tags = new ArrayList<>();

            articleTags.forEach(articleTag -> {
                Tag tag = articleTag.getTag();
                tags.add(tag.getName());
            });

            articleResponseDtos.add(ArticleResponseDto.of(article, tags));
        });

        return articleResponseDtos;
    }

    public List<ArticleResponseDto> findMyArticles(Long userId) {
        List<Article> articles = articleRepository.findByUserId(userId);
        List<ArticleResponseDto> articleResponseDtos = new ArrayList<>();

        articles.forEach(article -> {
            List<ArticleTag> articleTags = article.getArticleTags();
            List<String> tags = new ArrayList<>();

            articleTags.forEach(articleTag -> {
                Tag tag = articleTag.getTag();
                tags.add(tag.getName());
            });

            articleResponseDtos.add(ArticleResponseDto.of(article, tags));
        });

        return articleResponseDtos;
    }

    public ArticleResponseDto findArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("해당 articleId를 가진 스크랩 컨텐츠가 존재하지 않습니다."));

        List<ArticleTag> articleTags = article.getArticleTags();
        List<String> tags = new ArrayList<>();

        articleTags.forEach(articleTag -> {
            Tag tag = articleTag.getTag();
            tags.add(tag.getName());
        });

        return ArticleResponseDto.of(article, tags);
    }

    public void modifyArticle(Long articleId, ArticleModifyRequestDto articleModifyRequestDto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("해당 articleId를 가진 스크랩 컨텐츠가 존재하지 않습니다."));

        changeArticleInfo(article,
                articleModifyRequestDto.getUrl(),
                articleModifyRequestDto.getTitle(),
                articleModifyRequestDto.getDescription(),
                articleModifyRequestDto.getMemo());

        changeTags(article, articleModifyRequestDto.getTags());

        articleRepository.save(article);
    }

    public void modifyArticleListOfCollection(Long articleId, List<Long> collectionIdList) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("해당 articleId를 가진 아티클이 존재하지 않습니다."));

        List<ArticleCollection> originalArticleCollectionList = articleCollectionRepository.findByArticleId(articleId);

        List<ArticleCollection> modifiedArticleCollectionList = new ArrayList<>();
        collectionIdList.forEach(collectionId -> {
            Collection collection = collectionRepository.findById(collectionId)
                    .orElseThrow(() -> new RuntimeException("해당 collectionId를 가진 아티클이 존재하지 않습니다."));

            ArticleCollection articleCollection = articleCollectionRepository.findByArticleIdAndCollectionId(articleId, collectionId)
                    .orElse(new ArticleCollection(article, collection));
            modifiedArticleCollectionList.add(articleCollection);
        });

        changeCollection(originalArticleCollectionList, modifiedArticleCollectionList);
    }

    private void changeCollection(List<ArticleCollection> originalArticleCollectionList, List<ArticleCollection> modifiedArticleCollectionList) {
        List<ArticleCollection> newArticleCollectionList = modifiedArticleCollectionList.stream()
                .filter(articleCollection -> !originalArticleCollectionList.contains(articleCollection))
                .collect(Collectors.toList());

        newArticleCollectionList.forEach(articleCollection -> {
            articleCollectionRepository.save(articleCollection);
            articleCollection.getCollection().addArticleCount();
        });

        List<ArticleCollection> deletedArticleCollectionList = originalArticleCollectionList.stream()
                .filter(articleCollection -> !modifiedArticleCollectionList.contains(articleCollection))
                .collect(Collectors.toList());

        deletedArticleCollectionList.forEach(articleCollection -> {
            articleCollectionRepository.deleteById(articleCollection.getId());
            articleCollection.getCollection().minusArticleCount();
        });
    }

    public void deleteArticle(Long articleId) {
        if (articleRepository.existsById(articleId)) {
            articleRepository.deleteById(articleId);
        } else {
            throw new RuntimeException("해당 articleId를 가진 스크랩 컨텐츠가 존재하지 않습니다.");
        }
    }

    private void changeArticleInfo(Article article, String url, String title, String description, String memo) {
        article.changeUrl(url);
        article.changeTitle(title);
        article.changeDescription(description);
        article.changeMemo(memo);
    }

    private void changeTags(Article article, List<String> modifiedTagNames) {
        List<ArticleTag> articleTags = article.getArticleTags();

        List<Tag> originalTags = new ArrayList<>();
        articleTags.forEach(articleTag -> {
            originalTags.add(articleTag.getTag());
        });

        List<Tag> modifiedTags = new ArrayList<>();
        modifiedTagNames.forEach(name -> {
            Tag tag = tagRepository.findByName(name).orElse(null);
            if (tag == null) {
                tag = new Tag(name);
                tagRepository.save(tag);
            }
            modifiedTags.add(tag);
        });

        changeTags(article, originalTags, modifiedTags);
    }
    private void changeTags(Article article, List<Tag> originalTags, List<Tag> modifiedTags) {
        // 요청 태그 중, 기존 태그에 없는 태그인 새로운 태그 관련 ArticleTag 객체 추가
        List<Tag> newTags = modifiedTags.stream()
                .filter(tag -> !originalTags.contains(tag))
                .collect(Collectors.toList());
        newTags.forEach(tag -> articleTagRepository.save(new ArticleTag(article, tag)));

        // 기존 태그 중, 요청 태그에 없는 태그들. 즉 삭제되야 하는 태그들 관련 ArticleTag 객체 삭제
        List<Tag> deletedTags = originalTags.stream()
                .filter(tag -> !modifiedTags.contains(tag))
                .collect(Collectors.toList());
        deletedTags.forEach(tag -> {
            System.out.println("삭제할 태그: " + tag.getName());
            final ArticleTag articleTag = articleTagRepository.findByArticleIdAndTagId(article.getId(), tag.getId())
                    .orElse(null);

            // 아래 2줄 대신 deleteById()를 이용해, 부모 객체 내 체크를 하는 행동을 제외하여 즉각적인 삭제를 할 수 있다.
            // 이후 원주씨와 회의 예정
            article.getArticleTags().remove(articleTag);
            articleTagRepository.delete(articleTag);
        });
    }
}
