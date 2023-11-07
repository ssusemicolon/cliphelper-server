package com.example.cliphelper.domain.bookmark.service;

import com.example.cliphelper.domain.bookmark.dto.BookmarkRequestDto;
import com.example.cliphelper.domain.bookmark.entity.Bookmark;
import com.example.cliphelper.domain.collection.entity.Collection;
import com.example.cliphelper.domain.user.entity.User;
import com.example.cliphelper.domain.bookmark.repository.BookmarkRepository;
import com.example.cliphelper.domain.collection.repository.CollectionRepository;
import com.example.cliphelper.domain.user.repository.UserRepository;
import com.example.cliphelper.global.config.security.util.SecurityUtils;

import com.example.cliphelper.global.error.ErrorCode;
import com.example.cliphelper.global.error.exception.BookmarkMyselfException;
import com.example.cliphelper.global.error.exception.EntityNotFoundException;
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
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        Collection collection = collectionRepository.findById(bookmarkRequestDto.getCollectionId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.COLLECTION_NOT_FOUND));

        if (collection.getUser() == user) {
            throw new BookmarkMyselfException(ErrorCode.CANNOT_BOOKMARK_MY_COLLECTION);
        }
        bookmarkRepository.save(new Bookmark(user, collection));
    }

    public void deleteBookmark(Long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
    }
}
