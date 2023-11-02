package com.example.cliphelper.service;

import com.example.cliphelper.dto.BookmarkRequestDto;
import com.example.cliphelper.entity.Bookmark;
import com.example.cliphelper.entity.Collection;
import com.example.cliphelper.entity.User;
import com.example.cliphelper.repository.BookmarkRepository;
import com.example.cliphelper.repository.CollectionRepository;
import com.example.cliphelper.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookmarkService {
    private final UserRepository userRepository;
    private final CollectionRepository collectionRepository;
    private final BookmarkRepository bookmarkRepository;

    public void addBookmark(BookmarkRequestDto bookmarkRequestDto) {
        User user = userRepository.findById(bookmarkRequestDto.getUserId())
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
