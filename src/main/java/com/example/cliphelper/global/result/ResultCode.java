package com.example.cliphelper.global.result;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 결과 코드에 대한 Enum class
 * 결과 코드 형식
 * 1. 모든 결과 코드는 "S"로 시작한다.
 * 2. 2번째 문자는 결과가 발생한 카테고리를 나타낸다.
 * ex) "U": User, "A": Article, "C": Collection, "F": File, "AL" Alarm,
 *
 */
@Getter
@RequiredArgsConstructor
public enum ResultCode {

    // 회원 관련
    USER_JOIN_SUCCESS(200, "SU001", "회원가입이 성공적으로 완료되었습니다."),
    USER_LOGIN_SUCCESS(200, "SU002", "로그인이 성공적으로 완료되었습니다."),
    USER_FIND_SUCCESS(200, "SU003", "회원을 성공적으로 조회하였습니다."),
    ALL_USERS_FIND_SUCCESS(200, "SU004", "모든 회원들을 성공적으로 조회하였습니다."),
    USER_PROFILE_FIND_SUCCESS(200, "SU000", "회원의 프로필을 성공적으로 조회하였습니다."),
    USER_MODIFY_USERNAME_SUCCESS(200, "SU005", "회원의 닉네임를 성공적으로 수정하였습니다."),
    USER_MODIFY_PICTURE_SUCCESS(200, "SU006", "회원의 이미지를 성공적으로 수정하였습니다."),
    USER_DELETE_SUCCESS(200, "SU010", "입력한 userId의 회원을 성공적으로 삭제하였습니다."),
    USER_SIGN_OUT_SUCCESS(200, "SU011", "로그아웃에 성공했습니다."),
    USER_TOKEN_REFRESH_SUCCESS(200, "SU012", "토큰 재발급에 성공했습니다."),

    // 아티클(200, 스크랩 컨텐츠 관련)
    ARTICLE_CREATE_SUCCESS(200, "SA001", "입력한 아티클을 성공적으로 등록하였습니다."),
    ARTICLE_FIND_SUCCESS(200, "SA002", "입력한 articleId의 아티클을 성공적으로 조회하였습니다."),
    ALL_ARTICLES_FIND_SUCCESS(200, "SA003", "아티클 목록을 성공적으로 조회하였습니다."),
    ARTICLE_MODIFY_SUCCESS(200, "SA004", "입력한 postId의 아티클을 성공적으로 수정하였습니다."),
    ARTICLE_DELETE_SUCCESS(200, "SA010", "입력한 postId의 아티클을 성공적으로 삭제하였습니다"),

    // 컬렉션 관련
    COLLECTION_CREATE_SUCCESS(200, "SC001", "입력한 컬렉션을 성공적으로 생성하였습니다."),
    COLLECTION_FIND_SUCCESS(200, "SC002", "입력한 articleId의 아티클을 성공적으로 조회하였습니다."),
    ALL_COLLECTIONS_FIND_SUCCESS(200, "SC003", "전체 컬렉션 목록을 성공적으로 조회하였습니다."),
    MY_COLLECTIONS_FIND_SUCCESS(200, "SC004", "내 컬렉션 목록을 성공적으로 조회하였습니다."),
    OTHER_COLLECTIONS_FIND_SUCCESS(200, "SC005", "다른 회원들의 컬렉션 목록을 성공적으로 조회하였습니다."),
    COLLECTION_MODIFY_SUCCESS(200, "SC006", "입력한 collectionId의 컬렉션을 성공적으로 수정하였습니다."),
    COLLECTION_DELETE_SUCCESS(200, "SC010", "입력한 collectionId의 컬렉션을 성공적으로 삭제하였습니다"),
    ARTICLE_ADD_TO_COLLECTION_SUCCESS(200, "SC011", "해당 아티클을 컬렉션에 성공적으로 추가하였습니다."),

    // 북마크 관련,
    BOOKMARK_COLLECTION_SUCCESS(200, "SB001", "선택한 컬렉션을 성공적으로 북마크하였습니다."),
    BOOKMARK_FIND_SUCCESS(200, "SB002", "입력한 bookmarkId의 북마크 컬렉션을 성공적으로 조회하였습니다."),
    ALL_BOOKMARKS_FIND_SUCCESS(200, "SB003", "모든 북마크 컬렉션 목록을 성공적으로 조회하였습니다."),
    MY_BOOKMARKS_FIND_SUCCESS(200, "SB000", "사용자의 모든 북마크 컬렉션 목록을 성공적으로 조회하였습니다."),
    BOOKMARK_MODIFY_SUCCESS(200, "SB004", "입력한 collectionId의 북마크 컬렉션을 성공적으로 수정하였습니다."),
    BOOKMARK_DELETE_SUCCESS(200, "SB010", "입력한 collectionId의 북마크 컬렉션을 성공적으로 삭제하였습니다."),

    // 파일 관련
    FILE_UPLOAD_SUCCESS(200, "SF001", "해당 파일을 성공적으로 업로드하였습니다."),
    FILE_DOWNLOAD_SUCCESS(200, "SF002", "해당 파일을 성공적으로 다운로드하였습니다."),
    FILE_DELETE_SUCCESS(200, "SF005", "해당 파일을 성공적으로 삭제하였습니다."),

    // 푸시 알림 관련
    NOTIFICATION_TOKEN_REGISTER_SUCCESS(200, "SAL001", "회원의 현재 디바이스 토큰을 성공적으로 등록하였습니다."),
    ALLOW_NOTIFICATIONS_SUCCESS(200, "SAL000", "알람 허용이 성공적으로 완료되었습니다."),
    ALARM_TIME_ADD_SUCCESS(200, "SAL000", "알림을 받을 희망 시간대를 성공적으로 추가하였습니다."),
    ALARM_TIME_MODIFY_SUCCESS(200, "SAL000", "알림을 받을 희망 시간대를 성공적으로 변경하였습니다."),
    ALARM_TIME_DELETE_SUCCESS(200, "SAL000", "입력한 알람 희망 시간대를 성공적으로 삭제하였습니다."),
    ALARM_TIME_LIST_FIND_SUCCESS(200, "SAL000", "해당 회원의 알람 희망 시간대 목록을 성공적으로 조회하였습니다."),
    ALARM_SEND_SUCCESS(200, "SAL003", "푸시 알림을 성공적으로 전송하였습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}