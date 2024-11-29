package com.sparta.trelloproject.domain.card.dto.reponse;

import com.sparta.trelloproject.domain.card.entity.Card;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CardDetailResponseDto {

    private final Long id;
    private final String title;
    private final String contents;
    private final LocalDateTime dueDate;

    public CardDetailResponseDto(Long id, String title, String contents, LocalDateTime dueDate) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.dueDate = dueDate;
    }

    // 필요한 필드만 사용해서 CardResponseDto를 반환하는 메서드
    public static CardDetailResponseDto from(Card card) {
        return new CardDetailResponseDto(
                card.getId(),
                card.getTitle(),
                card.getContents(),
                card.getDueDate()
        );
    }
}
