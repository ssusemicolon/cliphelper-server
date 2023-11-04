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

    USER_NOT_FOUND(404, "EU001", "해당 회원이 존재하지 않습니다."),
    PASSWORD_NOT_MATCH(401, "EU002", "비밀번호가 일치하지 않습니다."),
    AUTHENTICATION_FAILED(401, "EU003", "로그인에 실패하였습니다."),
    JWT_AUTHENTICATION_FAILED(401, "EU005", "JWT 인증에 실패하였습니다."),
    JWT_EXPIRED(401, "EU006", "만료된 토큰입니다."),

    ARTICLE_NOT_FOUND(404, "EA001", "해당 아티클이 존재하지 않습니다."),
    COLLECTION_NOT_FOUND(404, "EC001", "해당 컬렉션이 존재하지 않습니다."),
    FILE_CANNOT_UPLOAD(400, "EF001", "해당 파일을 업로드할 수 없습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}