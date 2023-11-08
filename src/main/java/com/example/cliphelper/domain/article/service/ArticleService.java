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
import com.example.cliphelper.global.config.security.util.SecurityUtils;
import com.example.cliphelper.global.error.ErrorCode;
import com.example.cliphelper.global.error.exception.EntityNotFoundException;
import com.example.cliphelper.global.error.exception.FileNotModifiedException;
import com.example.cliphelper.global.service.FileService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    private final FileService fileService;
    private final SecurityUtils securityUtils;

    public void createArticle(ArticleRequestDto articleRequestDto) {
        Article article = articleRequestDto.toEntity();
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        article.setUser(user);

        System.out.println("======articleRequestDto.getTitle():" + articleRequestDto.getTitle());

        // 스크랩 컨텐츠가 파일인 경우
        MultipartFile file = articleRequestDto.getFile();
        if (file != null) {
            String uuid = UUID.randomUUID().toString();
            String fileUrl = fileService.uploadFile(file, uuid);
            article.setFileInfo(fileUrl, uuid);
            article.changeTitle(file.getOriginalFilename());
        }

        articleRepository.save(article);
        // Tag 엔티티, ArticleTag 엔티티 save
        tagService.registerTagInArticle(article, articleRequestDto.getTags());
    }

    public List<ArticleResponseDto> findAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return ArticleResponseDto.ofList(articles);
    }

    public List<ArticleResponseDto> findMyArticles() {
        List<Article> articles = articleRepository.
                findByUserIdOrderByCreatedAtDesc(securityUtils.getCurrentUserId());
        return ArticleResponseDto.ofList(articles);
    }

    public ArticleResponseDto findArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND));
        return ArticleResponseDto.of(article);
    }

    public void modifyArticle(Long articleId, ArticleModifyRequestDto articleModifyRequestDto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND));

        changeArticleInfo(article,
                articleModifyRequestDto.getUrl(),
                articleModifyRequestDto.getThumbnail(),
                articleModifyRequestDto.getTitle(),
                articleModifyRequestDto.getDescription(),
                articleModifyRequestDto.getMemo()
        );

        changeTags(article, articleModifyRequestDto.getTags());

        articleRepository.save(article);
    }

    public void modifyArticleListOfCollection(Long articleId, List<Long> collectionIdList) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND));

        List<ArticleCollection> originalArticleCollectionList = articleCollectionRepository.findByArticleId(articleId);

        List<ArticleCollection> modifiedArticleCollectionList = new ArrayList<>();
        collectionIdList.forEach(collectionId -> {
            Collection collection = collectionRepository.findById(collectionId)
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND));

            ArticleCollection articleCollection = articleCollectionRepository
                    .findByArticleIdAndCollectionId(articleId, collectionId)
                    .orElse(new ArticleCollection(article, collection));
            modifiedArticleCollectionList.add(articleCollection);
        });

        changeCollection(originalArticleCollectionList, modifiedArticleCollectionList);
    }

    private void changeCollection(List<ArticleCollection> originalArticleCollectionList,
            List<ArticleCollection> modifiedArticleCollectionList) {
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
            Article article = articleRepository.findById(articleId).orElse(null);

            // 파일 아티클인 경우 파일을 S3에서 삭제
            if (article.getFileUrl() != null) {
                fileService.deleteFile(article.getUuid() + article.getTitle());
            }
            articleRepository.deleteById(articleId);
        } else {
            throw new EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND);
        }
    }

    private void changeArticleInfo(Article article, String url, String thumbnail, String title, String description, String memo) {
        if (article.getFileUrl() != null) {
            throw new FileNotModifiedException(ErrorCode.FILE_CANNOT_MODIFIED);
        }

        article.changeUrl(url);
        article.changeThumbnail(thumbnail);
        article.changeTitle(title);
        article.changeDescription(description);
        article.changeMemo(memo);
    }

    private void changeTags(Article article, List<String> modifiedTagNames) {
        List<Tag> originalTags = article.getTags()
                .stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TAG_NOT_FOUND)))
                .collect(Collectors.toList());

        List<Tag> modifiedTags = new ArrayList<>();
        modifiedTagNames.forEach(name -> {
            Tag tag = tagRepository.findByName(name)
                    .orElse(null);
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

            article.getArticleTags().remove(articleTag);
            articleTagRepository.delete(articleTag);
        });
    }
}
