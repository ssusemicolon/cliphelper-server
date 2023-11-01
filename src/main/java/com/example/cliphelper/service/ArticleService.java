package com.example.cliphelper.service;

import com.example.cliphelper.dto.ArticleRequestDto;
import com.example.cliphelper.dto.ArticleResponseDto;
import com.example.cliphelper.entity.Article;
import com.example.cliphelper.entity.ArticleTag;
import com.example.cliphelper.entity.Tag;
import com.example.cliphelper.entity.User;
import com.example.cliphelper.repository.ArticleRepository;
import com.example.cliphelper.repository.ArticleTagRepository;
import com.example.cliphelper.repository.TagRepository;
import com.example.cliphelper.repository.UserRepository;
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

    public void deleteArticle(Long articleId) {
        if (articleRepository.existsById(articleId)) {
            articleRepository.deleteById(articleId);
        } else {
            throw new RuntimeException("해당 articleId를 가진 스크랩 컨텐츠가 존재하지 않습니다.");
        }
    }

    public void modifyArticle(Long articleId, ArticleRequestDto articleRequestDto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("해당 articleId를 가진 스크랩 컨텐츠가 존재하지 않습니다."));

        changeArticleInfo(article, articleRequestDto);
        articleRepository.save(article);
    }

    private void changeArticleInfo(Article article, ArticleRequestDto articleRequestDto) {
        // null 체크 후, null이 아닌 경우만 변경
        // =================================================================
        // 근데, null이 가능한 데이터는?
        // 예시로 기존에 작성한 데이터(메모)를 지우고 싶어서 null로 보낼 땐 어떡하지? 해결 필요
        // =================================================================

        if (articleRequestDto.getUrl() != null) {
            article.changeUrl(articleRequestDto.getUrl());
        }

        if (articleRequestDto.getTitle() != null) {
            article.changeTitle(articleRequestDto.getTitle());
        }

        if (articleRequestDto.getDescription() != null) {
            article.changeDescription(articleRequestDto.getDescription());
        }

        if (articleRequestDto.getMemo() != null) {
            article.changeMemo(articleRequestDto.getMemo());
        }

        changeTags(article, articleRequestDto.getTags());
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

        // [A, B, C] 기존 태그
        // [A, C, D] 수정 요청 태그
    }

}
