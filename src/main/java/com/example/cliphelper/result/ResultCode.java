package com.example.cliphelper.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 결과 코드에 대한 Enum class
 * 결과 코드 형식
 * 1. 모든 결과 코드는 "S"로 시작한다.
 * 2. 2번째 문자는 결과가 발생한 카테고리를 나타낸다.
 *   ex) "U": User, "A": Article, "C": Collection, "F": File, "AL" Alarm,
 *
 */
@Getter
@RequiredArgsConstructor
public enum ResultCode {

    // 회원 관련
    USER_JOIN_SUCCESS("SU001", "회원가입이 성공적으로 완료되었습니다."),
    USER_LOGIN_SUCCESS("SU002", "로그인이 성공적으로 완료되었습니다."),
    ALL_USERS_FIND_SUCCESS("SU003", "모든 사용자들을 성공적으로 조회하였습니다."),

    // 아티클(스크랩 컨텐츠 관련)
    ARTICLE_CREATE_SUCCESS("SA001", "입력한 아티클을 성공적으로 등록하였습니다." ),
    ARTICLE_FIND_SUCCESS("SA002", "입력한 articleId의 아티클을 성공적으로 조회하였습니다."),
    ALL_ARTICLES_FIND_SUCCESS("SA003", "아티클 목록을 성공적으로 조회하였습니다."),
    ARTICLE_MODIFY_SUCCESS("SA004", "입력한 postId의 아티클을 성공적으로 수정하였습니다."),
    ARTICLE_DELETE_SUCCESS("SA005", "입력한 postId의 아티클을 성공적으로 삭제하였습니다"),

    // 컬렉션 관련
    COLLECTION_CREATE_SUCCESS("SC001", "입력한 컬렉션을 성공적으로 생성하였습니다." ),
    COLLECTION_FIND_SUCCESS("SC002", "입력한 articleId의 아티클을 성공적으로 조회하였습니다."),
    ALL_COLLECTIONS_FIND_SUCCESS("SC003", "컬렉션 목록을 성공적으로 조회하였습니다."),
    COLLECTION_MODIFY_SUCCESS("SC004", "입력한 collectionId의 컬렉션을 성공적으로 수정하였습니다."),
    COLLECTION_DELETE_SUCCESS("SC005", "입력한 collectionId의 컬렉션을 성공적으로 삭제하였습니다"),
    ARTICLE_ADD_TO_COLLECTION_SUCCESS("SC006", "해당 아티클을 컬렉션에 성공적으로 추가하였습니다."),

    // 북마크 관련
    BOOKMARK_COLLECTION_SUCCESS("SB001", "선택한 컬렉션을 성공적으로 북마크하였습니다."),
    BOOKMARK_FIND_SUCCESS("SB002", "입력한 bookmarkId의 북마크 컬렉션을 성공적으로 조회하였습니다."),
    ALL_BOOKMARKS_FIND_SUCCESS("SB003", "모든 북마크 컬렉션 목록을 성공적으로 조회하였습니다."),
    BOOKMARK_MODIFY_SUCCESS("SB004", "입력한 collectionId의 북마크 컬렉션을 성공적으로 수정하였습니다."),
    BOOKMARK_DELETE_SUCCESS("SB005", "입력한 collectionId의 북마크 컬렉션을 성공적으로 삭제하였습니다."),

    // 푸시 알림 관련
    ALARM_SEND_SUCCESS("SAL001", "푸시 알림을 성공적으로 전송하였습니다."),

    ;

    private final String code;
    private final String message;
}