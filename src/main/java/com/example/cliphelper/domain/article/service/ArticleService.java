package com.example.cliphelper.domain.article.service;

import com.example.cliphelper.domain.article.dto.ArticleModifyRequestDto;
import com.example.cliphelper.domain.article.dto.ArticleRequestDto;
import com.example.cliphelper.domain.article.dto.ArticleResponseDto;
import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.collection.entity.ArticleCollection;
import com.example.cliphelper.domain.tag.entity.ArticleTag;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    @Transactional
    public void createArticle(ArticleRequestDto articleRequestDto) {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        final Article article;
        MultipartFile file = articleRequestDto.getFile();
        if (file != null) { // 파일 아티클인 경우
            String uuid = UUID.randomUUID().toString();
            String fileUrl = fileService.uploadFile(file, uuid);

            article = articleRequestDto.toEntity(fileUrl, uuid, user);
            article.changeTitle(file.getOriginalFilename());
        } else { // 일반 아티클인 경우
            article = articleRequestDto.toEntity(user);
        }

        articleRepository.save(article);
        // Tag 엔티티, ArticleTag 엔티티 save
        if (articleRequestDto.getTags() != null) {
            tagService.registerTagInArticle(article, articleRequestDto.getTags());
        }
    }

    public List<ArticleResponseDto> findAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return ArticleResponseDto.ofList(articles);
    }

    public List<ArticleResponseDto> findMyArticles() {
        List<Article> articles = articleRepository.findByUserIdOrderByCreatedAtDesc(securityUtils.getCurrentUserId());
        return ArticleResponseDto.ofList(articles);
    }

    @Transactional
    public ArticleResponseDto findArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND));

        article.updateRecentAccessTime();
        articleRepository.flush();
        return ArticleResponseDto.of(article);
    }

    public ArticleResponseDto findOldestUnseenArticle(Long userId) {
        Article article = articleRepository.findTopByUserIdOrderByRecentAccessTimeAsc(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND));

        return ArticleResponseDto.of(article);
    }

    @Transactional
    public void modifyArticle(Long articleId, ArticleModifyRequestDto articleModifyRequestDto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND));

        if (article.getFileUrl() != null) {
            throw new FileNotModifiedException(ErrorCode.FILE_CANNOT_MODIFIED);
        }

        changeArticleInfo(article,
                articleModifyRequestDto.getUrl(),
                articleModifyRequestDto.getThumbnail(),
                articleModifyRequestDto.getTitle(),
                articleModifyRequestDto.getDescription(),
                articleModifyRequestDto.getMemo());

        changeTags(article, articleModifyRequestDto.getTags());
        articleRepository.flush();
    }

    private void changeArticleInfo(Article article, String url, String thumbnail,
                                   String title, String description, String memo) {
        article.changeUrl(url);
        article.changeThumbnail(thumbnail);
        article.changeTitle(title);
        article.changeDescription(description);
        article.changeMemo(memo);
    }

    private void changeTags(Article article, List<String> modifiedTagNames) {
        List<Tag> originalTags = articleTagRepository.findByArticleId(article.getId())
                .stream()
                .map(articleTag -> articleTag.getTag())
                .collect(Collectors.toList());

        List<Tag> modifiedTags = modifiedTagNames
                .stream()
                .map(tagName -> tagService.registerTag(tagName))
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.TAG_NOT_FOUND)))
                .collect(Collectors.toList());

        changeTags(article, originalTags, modifiedTags);
    }

    private void changeTags(Article article, List<Tag> originalTags, List<Tag> modifiedTags) {
        // 요청 태그 중, 기존 태그에 없는 태그인 새로운 태그 관련 ArticleTag 객체 추가
        List<Tag> newTags = modifiedTags
                .stream()
                .filter(tag -> !originalTags.contains(tag))
                .collect(Collectors.toList());
        newTags.forEach(tag -> articleTagRepository.save(new ArticleTag(article, tag)));

        // 기존 태그 중, 요청 태그에 없는 태그들. 즉 삭제되야 하는 태그들 관련 ArticleTag 객체 삭제
        List<Tag> deletedTags = originalTags
                .stream()
                .filter(tag -> !modifiedTags.contains(tag))
                .collect(Collectors.toList());
        deletedTags.forEach(tag -> {
            final ArticleTag articleTag = articleTagRepository.findByArticleIdAndTagId(article.getId(), tag.getId())
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ARTICLETAG_NOT_FOUND));

            // 현재 파라미터로 Article 객체를 전달받는다.
            // POJO 에서 동기화 오류 가능성을 방지하기 위해, 해당 객체와 연관된 ArticleTag를 delete하려 하면 delete되지 않는다.
            // 이를 원활하게 처리하기 위해, 리스트에서 삭제 후, delete 실행
            // deleteById()를 이용해도 된다.
            article.getArticleTags().remove(articleTag);
            articleTagRepository.delete(articleTag);
        });
    }

    public List<Long> getArticleListOfCollection(Long articleId) {
        List<ArticleCollection> originalArticleCollectionList = articleCollectionRepository.findByArticleId(articleId);
        return originalArticleCollectionList.stream().map(ol -> ol.getCollection().getId())
                .collect(Collectors.toList());
    }

    @Transactional
    public void modifyArticleListOfCollection(Long articleId, List<Long> collectionIdList) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND));

        List<ArticleCollection> originalArticleCollectionList = articleCollectionRepository.findByArticleId(articleId);

        List<ArticleCollection> modifiedArticleCollectionList = collectionIdList
                .stream()
                .map(collectionId -> collectionRepository.findById(collectionId)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND)))
                .map(collection -> articleCollectionRepository.findByArticleIdAndCollectionId(
                                articleId,
                                collection.getId())
                        .orElse(new ArticleCollection(article, collection)))
                .collect(Collectors.toList());

        changeCollection(originalArticleCollectionList, modifiedArticleCollectionList);
    }

    private void changeCollection(List<ArticleCollection> originalArticleCollectionList,
            List<ArticleCollection> modifiedArticleCollectionList) {

        List<ArticleCollection> newArticleCollectionList = modifiedArticleCollectionList
                .stream()
                .filter(articleCollection -> !originalArticleCollectionList.contains(articleCollection))
                .collect(Collectors.toList());

        newArticleCollectionList.forEach(articleCollection -> {
            articleCollection.getCollection().addArticleCount();
            articleCollectionRepository.save(articleCollection);
        });

        List<ArticleCollection> deletedArticleCollectionList = originalArticleCollectionList
                .stream()
                .filter(articleCollection -> !modifiedArticleCollectionList.contains(articleCollection))
                .collect(Collectors.toList());

        deletedArticleCollectionList.forEach(articleCollection -> {
            articleCollection.getCollection().minusArticleCount();
            articleCollectionRepository.deleteById(articleCollection.getId());
        });
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND));

        // 파일 아티클인 경우 S3에서 파일을 삭제
        if (article.getFileUrl() != null) {
            fileService.deleteFile(article.getUuid() + article.getTitle());
        }

        articleRepository.deleteById(articleId);
    }

    public Long getArticleCountByUserId(Long userId) {
        return articleRepository.countByUserId(userId);
    }
}
