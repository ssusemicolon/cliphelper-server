package com.example.cliphelper.domain.collection.service;

import com.example.cliphelper.domain.bookmark.dto.BookmarkResponseDto;
import com.example.cliphelper.domain.collection.dto.CollectionModifyRequestDto;
import com.example.cliphelper.domain.collection.dto.CollectionRequestDto;
import com.example.cliphelper.domain.collection.dto.CollectionResponseDto;
import com.example.cliphelper.domain.article.entity.Article;
import com.example.cliphelper.domain.collection.entity.ArticleCollection;
import com.example.cliphelper.domain.bookmark.entity.Bookmark;
import com.example.cliphelper.domain.collection.entity.Collection;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.collection.repository.ArticleCollectionRepository;
import com.example.cliphelper.domain.article.repository.ArticleRepository;
import com.example.cliphelper.domain.collection.repository.CollectionRepository;
import com.example.cliphelper.domain.user.repository.UserRepository;
import com.example.cliphelper.global.config.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CollectionService {
    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleCollectionRepository articleCollectionRepository;
    private final SecurityUtils securityUtils;

    // 컬렉션 등록, 수정 시 모든 article을 추가할 수 있는 상태임.
    // article을 컬렉션에 넣기 전에, 내 아티클인지 확인하는 로직을 넣어야 한다고 생각함.
    public void createCollection(CollectionRequestDto collectionRequestDto) {
        Collection collection = collectionRequestDto.toEntity();
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("해당 userId를 가진 회원이 존재하지 않습니다."));
        collection.setUser(user);
        collectionRepository.save(collection);

        collectionRequestDto.getArticles().forEach(articleId -> {
            Article article = articleRepository.findById(articleId)
                    .orElseThrow(() -> new RuntimeException("해당 articleId를 가진 아티클이 존재하지 않습니다."));

            ArticleCollection articleCollection = new ArticleCollection(article, collection);
            articleCollectionRepository.save(articleCollection);
            // collection의 articleCollection에도 add해야 하는가?
            // collection.getArticleCollections().add(articleCollection);
            collection.addArticleCount();
        });

        collectionRepository.flush();
    }

    public List<CollectionResponseDto> readAllCollections() {
        List<CollectionResponseDto> collectionResponseDtos = new ArrayList<>();
        List<Collection> collections = collectionRepository.findAll();

        collections.forEach(collection -> {
            List<Long> articleIdList = new ArrayList<>();
            collection.getArticleCollections().forEach(articleCollection -> {
                Article article = articleCollection.getArticle();
                articleIdList.add(article.getId());
            });
            collectionResponseDtos.add(CollectionResponseDto.of(collection, articleIdList));
        });

        return collectionResponseDtos;
    }

    public CollectionResponseDto readCollection(Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("해당 collectionId를 가진 컬렉션이 존재하지 않습니다."));

        List<Long> articleIdList = new ArrayList<>();
        collection.getArticleCollections().forEach(articleCollection -> {
            Article article = articleCollection.getArticle();
            articleIdList.add(article.getId());
        });

        return CollectionResponseDto.of(collection, articleIdList);
    }

    public List<CollectionResponseDto> readMyCollections() {
        List<CollectionResponseDto> collectionResponseDtos = new ArrayList<>();
        List<Collection> collections = collectionRepository.findByUserId(securityUtils.getCurrentUserId());

        collections.forEach(collection -> {
            List<Long> articleIdList = new ArrayList<>();
            collection.getArticleCollections().forEach(articleCollection -> {
                Article article = articleCollection.getArticle();
                articleIdList.add(article.getId());
            });
            collectionResponseDtos.add(CollectionResponseDto.of(collection, articleIdList));
        });

        return collectionResponseDtos;
    }

    public List<BookmarkResponseDto> readMyBookmarkCollections() {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("해당 userId를 가진 회원이 존재하지 않습니다."));

        List<BookmarkResponseDto> bookmarkResponseDtos = new ArrayList<>();

        List<Bookmark> bookmarks = user.getBookmarks();
        bookmarks.forEach(bookmark -> {
            Collection collection = bookmark.getCollection();
            List<Long> articleIdList = new ArrayList<>();

            collection.getArticleCollections().forEach(articleCollection -> {
                Article article = articleCollection.getArticle();
                articleIdList.add(article.getId());
            });
            bookmarkResponseDtos.add(BookmarkResponseDto.of(collection, articleIdList));
        });

        return bookmarkResponseDtos;
    }

    public void modifyCollectionInfo(Long collectionId, CollectionModifyRequestDto collectionModifyRequestDto) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("해당 collectionId를 가진 컬렉션이 존재하지 않습니다."));

        collection.changeInfo(
                collectionModifyRequestDto.getTitle(),
                collectionModifyRequestDto.getDescription(),
                collectionModifyRequestDto.getIsPublic());

        collectionRepository.save(collection);
    }

    public void deleteArticleInCollection(Long collectionId, Long articleId) {
        if (!collectionRepository.existsById(collectionId)) {
            throw new RuntimeException("해당 collecitonId를 가진 컬렉션이 존재하지 않습니다.");
        }

        ArticleCollection articleCollection = articleCollectionRepository
                .findByArticleIdAndCollectionId(articleId, collectionId)
                .orElseThrow(() -> new RuntimeException("해당 컬렉션에 해당 articleId를 가진 아티클이 존재하지 않습니다."));

        articleCollectionRepository.deleteById(articleCollection.getId());
    }

    public void deleteCollection(Long collectionId) {
        if (collectionRepository.existsById(collectionId)) {
            collectionRepository.deleteById(collectionId);
        } else {
            throw new RuntimeException("해당 collectionId를 가진 컬렉션이 존재하지 않습니다.");
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
