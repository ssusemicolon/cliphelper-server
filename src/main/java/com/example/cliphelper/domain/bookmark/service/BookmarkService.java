package com.example.cliphelper.domain.bookmark.service;

import com.example.cliphelper.domain.bookmark.dto.BookmarkRequestDto;
import com.example.cliphelper.domain.bookmark.entity.Bookmark;
import com.example.cliphelper.domain.collection.entity.Collection;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.bookmark.repository.BookmarkRepository;
import com.example.cliphelper.domain.collection.repository.CollectionRepository;
import com.example.cliphelper.domain.user.repository.UserRepository;
import com.example.cliphelper.global.utils.service.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookmarkService {
    private final UserRepository userRepository;
    private final CollectionRepository collectionRepository;
    private final BookmarkRepository bookmarkRepository;
    private final SecurityUtils securityUtils;

    public void addBookmark(BookmarkRequestDto bookmarkRequestDto) {
        User user = userRepository.findById(securityUtils.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("해당 userId를 가진 회원이 존재하지 않습니다."));

        Collection collection = collectionRepository.findById(bookmarkRequestDto.getCollectionId())
                .orElseThrow(() -> new RuntimeException("해당 collectionId를 가진 컬렉션이 존재하지 않습니다."));

        // 자신의 컬렉션을 북마크하려는 경우, 예외 발생
        if (collection.getUser() == user) {
            throw new RuntimeException("자신의 컬렉션을 북마크할 수 없습니다.");
        }

        bookmarkRepository.save(new Bookmark(user, collection));
    }

    public void deleteBookmark(Long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
    }
}