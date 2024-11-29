package com.sparta.trelloproject.domain.card.dto.reponse;

import com.sparta.trelloproject.domain.card.entity.Card;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CardListResponseDto {

    private final Long id;
    private final String title;
    private final String contents;
    private final LocalDateTime dueDate;

    public CardListResponseDto(Long id, String title, String contents, LocalDateTime dueDate) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.dueDate = dueDate;
    }

    public static CardListResponseDto of(Card card) {
        return new CardListResponseDto(card.getId(), card.getTitle(), card.getContents(),
            card.getDueDate());
    }
}

