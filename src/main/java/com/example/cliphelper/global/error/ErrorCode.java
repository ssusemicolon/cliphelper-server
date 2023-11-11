package com.example.cliphelper.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 에러 코드에 대한 Enum class
 * 에러 코드 형식
 * 1. 모든 에러 코드는 "E"로 시작한다.
 * 2. 2번째 문자는 에러가 발생한 카테고리를 나타낸다.
 * ex) "U": User, "A": Article, "C": Collection, "F": File
 *
 * 해당 클래스에서 에러 코드에 관한 메시지를 정의하지만, 불가피한 경우 타 메시지를 사용해
 * response를 전송할 수 있다.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Global
    NETWORK_NOT_CONNECTED(400, "EG001", "네트워크에 연결되어 있지 않습니다."),
    INVALID_INPUT(400, "EG002", "올바르지 않은 입력입니다."),

    // 회원 및 JWT
    USER_NOT_FOUND(404, "EU001", "해당 회원이 존재하지 않습니다."),
    PASSWORD_NOT_MATCH(401, "EU002", "비밀번호가 일치하지 않습니다."),
    AUTHENTICATION_FAILED(401, "EU003", "로그인에 실패하였습니다."),
    JWT_AUTHENTICATION_FAILED(401, "EU005", "JWT 인증에 실패하였습니다."),
    JWT_EXPIRED(401, "EU006", "만료된 토큰입니다."),
    LOGOUT_BY_ANOTHER(401, "EU006", "다른 환경에 의해 로그아웃되었습니다."),

    // 아티클
    ARTICLE_NOT_FOUND(404, "EA001", "해당 아티클이 존재하지 않습니다."),
    FILE_CANNOT_UPLOAD(400, "EF001", "해당 파일을 업로드할 수 없습니다."),
    FILE_CANNOT_MODIFIED(400, "EF002", "파일 아티클은 수정할 수 없습니다."),
    ARTICLE_TITLE_IS_BLANK(400, "EA002", "아티클의 제목은 필수로 입력해야 합니다."),

    // 태그
    TAG_NOT_FOUND(404, "ET001", "해당 태그가 존재하지 않습니다."),

    // 컬렉션
    COLLECTION_NOT_FOUND(404, "EC001", "해당 컬렉션이 존재하지 않습니다."),
    ARTICLE_IS_NOT_IN_COLLECTION(400, "EC002", "해당 컬렉션에 해당 articleId를 가진 아티클이 존재하지 않습니다."),

    // 북마크
    BOOKMARK_CANNOT_FOUND(404, "EB001", "해당 북마크가 존재하지 않습니다."),
    CANNOT_BOOKMARK_MY_COLLECTION(400, "EB003", "자신의 컬렉션을 북마크할 수 없습니다."),

    // 알람
    ALARM_TIME_NOT_FOUND(404, "EA000", "해당 알람 시간이 존재하지 않습니다."),
    ALARM_TIME_ALREADY_EXISTS(400, "EAL001", "해당 알람 시간이 이미 존재합니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}