package com.example.cliphelper.domain.collection.controller;

import com.example.cliphelper.domain.collection.dto.CollectionModifyRequestDto;
import com.example.cliphelper.domain.collection.dto.CollectionRequestDto;
import com.example.cliphelper.domain.collection.dto.CollectionResponseDto;
import com.example.cliphelper.global.result.ResultCode;
import com.example.cliphelper.global.result.ResultResponse;
import com.example.cliphelper.domain.collection.service.CollectionService;
import javax.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    // 특정 id를 가진 컬렉션 조회
    @GetMapping("/collections/{collectionId}")
    public ResultResponse readCollection(@PathVariable("collectionId") Long collectionId) {
        CollectionResponseDto collectionResponseDto = collectionService.readCollection(collectionId);
        return ResultResponse.of(ResultCode.COLLECTION_FIND_SUCCESS, collectionResponseDto);
    }

    // 내(가 생성한) 컬렉션 조회
    @GetMapping("/collections")
    public ResultResponse readMyCollections() {
        List<CollectionResponseDto> collectionResponseDtos = collectionService.readMyCollections();
        return ResultResponse.of(ResultCode.MY_COLLECTIONS_FIND_SUCCESS, collectionResponseDtos);
    }

    // 다른 회원들의 컬렉션 조회
    @GetMapping("/collections/other")
    public ResultResponse readOtherCollections() {
        List<CollectionResponseDto> collectionResponseDtos = collectionService.readOtherCollections();
        return ResultResponse.of(ResultCode.OTHER_COLLECTIONS_FIND_SUCCESS, collectionResponseDtos);
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
