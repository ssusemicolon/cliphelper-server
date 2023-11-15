package com.example.cliphelper.domain.collection.service;

import com.example.cliphelper.domain.bookmark.repository.BookmarkRepository;
import com.example.cliphelper.domain.collection.dto.CollectionModifyRequestDto;
import com.example.cliphelper.domain.collection.dto.CollectionRequestDto;
import com.example.cliphelper.domain.collection.dto.CollectionResponseDto;
import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.collection.entity.ArticleCollection;
import com.example.cliphelper.domain.collection.entity.Collection;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.collection.repository.ArticleCollectionRepository;
import com.example.cliphelper.domain.article.repository.ArticleRepository;
import com.example.cliphelper.domain.collection.repository.CollectionRepository;
import com.example.cliphelper.domain.user.repository.UserRepository;
import com.example.cliphelper.global.config.security.util.SecurityUtils;
import com.example.cliphelper.global.error.ErrorCode;
import com.example.cliphelper.global.error.exception.ArticleNotInCollectionException;
import com.example.cliphelper.global.error.exception.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CollectionService {
    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleCollectionRepository articleCollectionRepository;
    private final BookmarkRepository bookmarkRepository;
    private final SecurityUtils securityUtils;

    // 컬렉션 등록, 수정 시 모든 article을 추가할 수 있는 상태임.
    // article을 컬렉션에 넣기 전에, 내 아티클인지 확인하는 로직을 넣어야 한다고 생각함.
    public void createCollection(CollectionRequestDto collectionRequestDto) {
        Collection collection = collectionRequestDto.toEntity();
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        collection.setUser(user);
        collectionRepository.save(collection);

        collectionRequestDto.getArticles().forEach(articleId -> {
            Article article = articleRepository.findById(articleId)
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND));

            ArticleCollection articleCollection = new ArticleCollection(article, collection);
            articleCollectionRepository.save(articleCollection);
            // collection의 articleCollection에도 add해야 하는가?
            // collection.getArticleCollections().add(articleCollection);
            collection.addArticleCount();
        });

        collectionRepository.flush();
    }

    public List<CollectionResponseDto> readAllCollections() {
        List<Collection> collections = collectionRepository.findAll();

        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        List<Collection> userBookmarkCollections = user.getBookmarks()
                .stream()
                .map(bookmark -> bookmark.getCollection())
                .collect(Collectors.toList());

        // 회원의 북마크 여부를 확인하고, DTO에 추가하여 생성한다.
        return collections
                .stream()
                .map(collection -> CollectionResponseDto.of(
                        collection,
                        userBookmarkCollections.contains(collection)))
                .collect(Collectors.toList());
    }

    public CollectionResponseDto readCollection(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.COLLECTION_NOT_FOUND));

        return CollectionResponseDto.of(collection);
    }

    public List<CollectionResponseDto> readMyCollections() {
        List<CollectionResponseDto> collectionResponseDtos = new ArrayList<>();
        List<Collection> collections = collectionRepository.findByUserId(securityUtils.getCurrentUserId());

        collections.forEach(collection -> collectionResponseDtos.add(CollectionResponseDto.of(collection)));
        return collectionResponseDtos;
    }

    public List<CollectionResponseDto> readOtherCollections() {
        List<Collection> collections = collectionRepository.findByUserIdNotAndIsPublicIsTrue(securityUtils.getCurrentUserId());
        List<CollectionResponseDto> collectionResponseDtos = new ArrayList<>();

        collections.forEach(collection -> collectionResponseDtos.add(CollectionResponseDto.of(collection)));
        return collectionResponseDtos;
    }

    public List<CollectionResponseDto> readMyBookmarkCollections() {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        List<CollectionResponseDto> collectionResponseDtos =
                user.getBookmarks()
                        .stream()
                        .map(bookmark -> CollectionResponseDto.of(bookmark.getCollection()))
                        .collect(Collectors.toList());

        return collectionResponseDtos;
    }

    public void modifyCollectionInfo(Long collectionId, CollectionModifyRequestDto collectionModifyRequestDto) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ARTICLE_NOT_FOUND));

        collection.changeInfo(
                collectionModifyRequestDto.getTitle(),
                collectionModifyRequestDto.getDescription(),
                collectionModifyRequestDto.getIsPublic());

        collectionRepository.save(collection);
    }

    public void deleteArticleInCollection(Long collectionId, Long articleId) {
        if (!collectionRepository.existsById(collectionId)) {
            throw new EntityNotFoundException(ErrorCode.COLLECTION_NOT_FOUND);
        }

        ArticleCollection articleCollection = articleCollectionRepository
                .findByArticleIdAndCollectionId(articleId, collectionId)
                .orElseThrow(() -> new ArticleNotInCollectionException(ErrorCode.ARTICLE_IS_NOT_IN_COLLECTION));

        articleCollectionRepository.deleteById(articleCollection.getId());
    }

    public void deleteCollection(Long collectionId) {
        if (collectionRepository.existsById(collectionId)) {
            collectionRepository.deleteById(collectionId);
        } else {
            throw new EntityNotFoundException(ErrorCode.COLLECTION_NOT_FOUND);
        }
    }

    /*
     * public void addArticleInCollection(Long collectionId, Long articleId) {
     * Collection collection = collectionRepository.findById(collectionId)
     * .orElseThrow(() -> new
     * RuntimeException("해당 collecitonId를 가진 컬렉션이 존재하지 않습니다."));
     *
     * Article article = articleRepository.findById(articleId)
     * .orElseThrow(() -> new RuntimeException("해당 articleId를 가진 아티클이 존재하지 않습니다."));
     *
     * articleCollectionRepository.save(new ArticleCollection(article, collection));
     * }
     */
}
