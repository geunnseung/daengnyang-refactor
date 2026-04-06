package com.geunnseung.daengnyangrefactor.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    INVALID_LOGIN_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 유효하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹을 찾을 수 없습니다."),
    ALREADY_GROUP_MEMBER(HttpStatus.CONFLICT, "이미 참여 중인 그룹입니다."),
    GROUP_JOIN_APPLICATION_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 대기 중인 그룹 참여 신청이 있습니다."),
    GROUP_OWNER_REQUIRED(HttpStatus.FORBIDDEN, "그룹 소유자만 요청할 수 있습니다."),
    GROUP_JOIN_APPLICATION_ALREADY_DECIDED(HttpStatus.CONFLICT, "이미 처리된 그룹 참여 신청입니다."),
    GROUP_JOIN_APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹 참여 신청을 찾을 수 없습니다."),
    PET_NOT_FOUND(HttpStatus.NOT_FOUND, "반려동물을 찾을 수 없습니다."),
    PET_ALREADY_HAS_GROUP(HttpStatus.CONFLICT, "이미 그룹에 연결된 반려동물입니다."),
    PET_GROUP_LINKED(HttpStatus.CONFLICT, "그룹에 연결된 반려동물은 삭제할 수 없습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    UNSUPPORTED_FILE_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 파일 형식입니다."),
    FILE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "파일 크기가 너무 큽니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    PET_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    PET_POST_ACCESS_DENIED(HttpStatus.FORBIDDEN, "게시글을 삭제할 권한이 없습니다.");

    private final HttpStatus status;
    private final String message;
}