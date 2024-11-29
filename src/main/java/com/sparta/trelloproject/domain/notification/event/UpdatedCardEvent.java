package com.sparta.trelloproject.domain.notification.event;

public class UpdatedCardEvent {
    private final Long userId;
    private final Long cardId;

    public UpdatedCardEvent(Long userId,Long cardId){
        this.userId=userId;
        this.cardId=cardId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getCardId() {
        return cardId;
    }
}
