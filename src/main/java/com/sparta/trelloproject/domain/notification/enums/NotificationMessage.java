package com.sparta.trelloproject.domain.notification.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationMessage {

    //알림 구독 관련 메시지
    SUCCESS_SUBSCRIBE("알림 구독 설정을 하였습니다"),

    // 댓글 관련 메시지
    ADDED_COMMENT("새로운 댓글이 달렸습니다."),

    // 워크스페이스 관련 메시지
    INVITE_WORKSPACE("워크스페이스에 초대되었습니다."),

    //카드 관련 메시지
    UPDATE_CARD("카드가 수정되었습니다");

    private final String message;
}
