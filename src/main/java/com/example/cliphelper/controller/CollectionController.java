package com.example.cliphelper.controller;

import com.example.cliphelper.dto.BookmarkResponseDto;
import com.example.cliphelper.dto.CollectionModifyRequestDto;
import com.example.cliphelper.dto.CollectionRequestDto;
import com.example.cliphelper.dto.CollectionResponseDto;
import com.example.cliphelper.result.ResultCode;
import com.example.cliphelper.result.ResultResponse;
import com.example.cliphelper.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CollectionController {
    private final CollectionService collectionService;

    // 새 컬렉션 등록
    @PostMapping("/collections")
    public ResultResponse createCollection(@Valid @RequestBody CollectionRequestDto collectionRequestDto) {
        collectionService.createCollection(collectionRequestDto);
        return ResultResponse.of(ResultCode.COLLECTION_CREATE_SUCCESS);
    }

    // 모든 컬렉션 조회
    @GetMapping("/admin/collections")
    public ResultResponse readAllCollections() {
        List<CollectionResponseDto> collectionResponseDtos = collectionService.readAllCollections();
        return ResultResponse.of(ResultCode.ALL_COLLECTIONS_FIND_SUCCESS, collectionResponseDtos);
    }

    //  특정 id를 가진 컬렉션 조회
    @GetMapping("/collections/{collectionId}")
    public ResultResponse readCollection(@PathVariable("collectionId") Long collectionId) {
        CollectionResponseDto collectionResponseDto = collectionService.readCollection(collectionId);
        return ResultResponse.of(ResultCode.COLLECTION_FIND_SUCCESS, collectionResponseDto);
    }

    // 내(가 생성한) 컬렉션 조회
    @GetMapping("/collections")
    public ResultResponse readMyCollections(@RequestParam("userId") Long userId) {
        List<CollectionResponseDto> collectionResponseDtos = collectionService.readMyCollections(userId);
        return ResultResponse.of(ResultCode.MY_COLLECTIONS_FIND_SUCCESS, collectionResponseDtos);
    }

    // 내가 북마크한 컬렉션 조회
    @GetMapping("/bookmarks")
    public ResultResponse readMyBookmarkCollections(@RequestParam("userId") Long userId) {
        List<BookmarkResponseDto> bookmarkResponseDtos = collectionService.readMyBookmarkCollections(userId);
        return ResultResponse.of(ResultCode.MY_BOOKMARKS_FIND_SUCCESS, bookmarkResponseDtos);
    }

    // 특정 컬렉션의 정보를 수정
    @PatchMapping("/collections/{collectionId}")
    public ResultResponse modifyCollectionInfo(@PathVariable("collectionId") Long collectionId, @RequestBody CollectionModifyRequestDto collectionModifyRequestDto) {
        collectionService.modifyCollectionInfo(collectionId, collectionModifyRequestDto);
        return ResultResponse.of(ResultCode.COLLECTION_MODIFY_SUCCESS);
    }

    // 컬렉션 내의 아티클 목록에서 특정 아티클 삭제
    @PatchMapping("/collections/{collectionId}/article/{articleId}")
    public ResultResponse deleteArticleInCollection(@PathVariable("collectionId") Long collectionId, @PathVariable("articleId") Long articleId) {
        collectionService.deleteArticleInCollection(collectionId, articleId);
        return ResultResponse.of(ResultCode.COLLECTION_MODIFY_SUCCESS);
    }

    // 특정 collectionId를 가진 컬렉션 삭제
    @DeleteMapping("/collections/{collectionId}")
    public ResultResponse deleteCollection(@PathVariable("collectionId") Long collectionId) {
        collectionService.deleteCollection(collectionId);
        return ResultResponse.of(ResultCode.COLLECTION_DELETE_SUCCESS);
    }

}
