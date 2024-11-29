package com.sparta.trelloproject.domain.card.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardStatus {

    WAITING("대기"),
    IN_PROGRESS("진행중"),
    COMPLETED("완료");

    private final String description;
}
