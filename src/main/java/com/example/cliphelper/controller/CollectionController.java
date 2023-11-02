package com.example.cliphelper.controller;

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
    @GetMapping("/collections")
    public ResultResponse readAllCollections() {
        List<CollectionResponseDto> collectionResponseDtos = collectionService.readAllCollections();
        return ResultResponse.of(ResultCode.ALL_COLLECTIONS_FIND_SUCCESS, collectionResponseDtos);
    }
//
    // 내(가 생성한) 컬렉션 조회
    // 로그인 방식 변경 후, PathVariable 대신 JWT 통한 확인으로 변경함으로써 URI 변경해야 함
    @GetMapping("/collections/my/{userId}")
    public ResultResponse readMyCollections(@PathVariable("userId") Long userId) {
        List<CollectionResponseDto> collectionResponseDtos = collectionService.readMyCollections(userId);
        return ResultResponse.of(ResultCode.MY_COLLECTIONS_FIND_SUCCESS, collectionResponseDtos);
    }

    // 북마크한 컬렉션 조회
     @GetMapping("/collections/bookmark/{userId}")
     public ResultResponse readMyBookmarkCollections(@PathVariable("userId") Long userId) {
        List<CollectionResponseDto> collectionResponseDtos = collectionService.readMyBookmarkCollections(userId);
         return ResultResponse.of(ResultCode.MY_BOOKMARKS_FIND_SUCCESS, collectionResponseDtos);
     }


    //  특정 id를 가진 컬렉션 조회
    @GetMapping("/collections/{collectionId}")
    public ResultResponse readCollection(@PathVariable("collectionId") Long collectionId) {
        CollectionResponseDto collectionResponseDto = collectionService.readCollection(collectionId);
        return ResultResponse.of(ResultCode.COLLECTION_FIND_SUCCESS, collectionResponseDto);
    }

    // 특정 collectionId를 가진 컬렉션 수정
    // 컬렉션 info를 수정하는 로직
    // 컬렉션 내의 아티클을 추가/삭제하는 로직
    // 위 2개의 로직으로 나눠서 아예 URI를 분리할지, 로직만 분리하여 URI는 통합할지 고민..
    /*
    @PatchMapping("/collections/{collectionId}")
    public ResultResponse modifyCollection(@PathVariable("collectionId") Long collectionId, @Valid @RequestBody CollectionRequestDto collectionRequestDto) {
        collectionService.modifyCollection(collectionId, collectionRequestDto);
        return ResultResponse.of(ResultCode.COLLECTION_CREATE_SUCCESS);
    }
    */

    // 특정 collectionId를 가진 컬렉션 삭제
    @DeleteMapping("/collections/{collectionId}")
    public ResultResponse deleteCollection(@PathVariable("collectionId") Long collectionId) {
        collectionService.deleteCollection(collectionId);
        return ResultResponse.of(ResultCode.COLLECTION_DELETE_SUCCESS);
    }

}
