package com.sparta.trelloproject.domain.card.entity;

import com.sparta.trelloproject.domain.card.dto.request.CardImageRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "card_image")
@Getter
@NoArgsConstructor
public class CardImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String path;

    @NotNull
    private String fileName;

    @NotNull
    private String originName;

    @NotNull
    private String extension;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    private CardImage(String path, String fileName, String originName, String extension,
        Card card) {
        this.path = path;
        this.fileName = fileName;
        this.originName = originName;
        this.extension = extension;
        this.card = card;
    }

    public static CardImage of(CardImageRequestDto cardImageDto, Card card) {
        return new CardImage(cardImageDto.getPath(), cardImageDto.getFileName(),
            cardImageDto.getOriginName(), cardImageDto.getExtension(), card);
    }

    public void update(CardImageRequestDto cardImageDto) {
        this.path = cardImageDto.getPath();
        this.fileName = cardImageDto.getFileName();
        this.originName = cardImageDto.getOriginName();
        this.extension = cardImageDto.getExtension();
    }
}
