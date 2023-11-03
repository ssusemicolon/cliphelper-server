package com.example.cliphelper.domain.bookmark.controller;

import com.example.cliphelper.domain.bookmark.dto.BookmarkRequestDto;
import com.example.cliphelper.domain.bookmark.dto.BookmarkResponseDto;
import com.example.cliphelper.domain.collection.service.CollectionService;
import com.example.cliphelper.global.result.ResultCode;
import com.example.cliphelper.global.result.ResultResponse;
import com.example.cliphelper.domain.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookmarkController {
    private final BookmarkService bookmarkService;
    private final CollectionService collectionService;

    @PostMapping("/bookmarks")
    public ResultResponse addBookmark(@Valid @RequestBody BookmarkRequestDto bookmarkRequestDto) {
        bookmarkService.addBookmark(bookmarkRequestDto);
        return ResultResponse.of(ResultCode.BOOKMARK_COLLECTION_SUCCESS);
    }

    // 내가 북마크한 컬렉션 조회
    @GetMapping("/bookmarks")
    public ResultResponse readMyBookmarkCollections() {
        List<BookmarkResponseDto> bookmarkResponseDtos = collectionService.readMyBookmarkCollections();
        return ResultResponse.of(ResultCode.MY_BOOKMARKS_FIND_SUCCESS, bookmarkResponseDtos);
    }

    @DeleteMapping("/bookmarks/{bookmarkId}")
    public ResultResponse deleteBookmark(@PathVariable("bookmarkId") Long bookmarkId) {
        bookmarkService.deleteBookmark(bookmarkId);
        return ResultResponse.of(ResultCode.BOOKMARK_DELETE_SUCCESS);
    }
}
