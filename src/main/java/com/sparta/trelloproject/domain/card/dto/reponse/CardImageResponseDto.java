package com.sparta.trelloproject.domain.card.dto.reponse;

import com.sparta.trelloproject.domain.card.entity.CardImage;
import lombok.Getter;

@Getter
public class CardImageResponseDto {

    private String path;

    private CardImageResponseDto(String path) {
        this.path = path;
    }

    public static CardImageResponseDto from(CardImage cardImage) {
        return new CardImageResponseDto(cardImage.getPath());
    }
}
