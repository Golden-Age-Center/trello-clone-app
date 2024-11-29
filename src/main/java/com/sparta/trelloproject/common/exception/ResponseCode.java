package com.sparta.trelloproject.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    // 공통
    SUCCESS("정상 처리되었습니다."),
    INVALID_TIMEOUT("다시 시도해주세요."),
    FORBIDDEN("접근 권한이 없습니다."),

    // 사용자
    NOT_FOUND_USER("해당 사용자는 존재하지 않습니다."),
    INVALID_USER_AUTHORITY("해당 사용자 권한은 유효하지 않습니다."),
    DUPLICATE_EMAIL("이미 존재하는 이메일입니다."),
    WRONG_EMAIL_OR_PASSWORD("이메일 혹은 비밀번호가 일치하지 않습니다."),
    WRONG_PASSWORD("비밀번호가 일치하지 않습니다."),

    // 워크스페이스
    NOT_FOUND_WORKSPACE("해당 워크스페이스는 존재하지 않습니다."),


    // 보드
    NOT_FOUND_BOARD("해당 보드는 존재하지 않습니다."),


    // 리스트
    NOT_FOUND_LIST("해당 리스트는 존재하지 않습니다."),
    INVALID_LIST_SEQUENCE("순서는 리스트 개수보다 클 수 없습니다."),

    // 카드
    NOT_FOUND_CARD("해당 카드는 존재하지 않습니다."),
    INVALID_UPLOAD("등록 오류입니다. 다시 시도해주세요."),
    INVALID_EXTENTSION("해당 확장자는 업로드가 불가능합니다"),

    // 댓글
    NOT_FOUND_COMMENT("해당 댓글은 존재하지 않습니다."),


    // 첨부파일
    NOT_FOUND_ATTACH_FILE("해당 첨부파일은 존재하지 않습니다."),


    // 알림
    CONNECTION_ERROR("연결 오류가 발생했습니다."),
    NOT_FOUND_EMITTER("해당하는 Emitter를 찾을 수 없습니다."),




    // 매니저
    MANAGER_ALREADY_EXISTS("이미 매니저가 존재합니다.");

    private final String message;
}
