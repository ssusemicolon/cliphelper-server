package com.example.cliphelper.controller;

import com.example.cliphelper.dto.BookmarkRequestDto;
import com.example.cliphelper.result.ResultCode;
import com.example.cliphelper.result.ResultResponse;
import com.example.cliphelper.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("/collections/bookmark")
    public ResultResponse addBookmark(@Valid @RequestBody BookmarkRequestDto bookmarkRequestDto) {
        bookmarkService.addBookmark(bookmarkRequestDto);
        return ResultResponse.of(ResultCode.BOOKMARK_COLLECTION_SUCCESS);
    }

    @DeleteMapping("/collections/bookmark/{bookmarkId}")
    public ResultResponse deleteBookmark(@PathVariable("bookmarkId") Long bookmarkId) {
        bookmarkService.deleteBookmark(bookmarkId);
        return ResultResponse.of(ResultCode.BOOKMARK_DELETE_SUCCESS);
    }
}
