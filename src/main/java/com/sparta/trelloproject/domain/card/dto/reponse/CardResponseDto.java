package com.sparta.trelloproject.domain.card.dto.reponse;

import com.sparta.trelloproject.domain.card.entity.Card;
import com.sparta.trelloproject.domain.comment.dto.response.UpdateCommentResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class CardResponseDto {

    private final Long id;
    private final String title;
    private final String contents;
    private final LocalDateTime dueDate;
    private final List<UpdateCommentResponseDto> comments;
    private CardImageResponseDto cardImage;

    private CardResponseDto(Long id, String title, String contents, LocalDateTime dueDate,
        List<UpdateCommentResponseDto> comments, CardImageResponseDto cardImage) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.dueDate = dueDate;
        this.comments = comments;
        this.cardImage = cardImage;
    }

    public static CardResponseDto of(Card card, List<UpdateCommentResponseDto> comments,
        CardImageResponseDto cardImage) {
        return new CardResponseDto(
            card.getId(),
            card.getTitle(),
            card.getContents(),
            card.getDueDate(),
            comments,
            cardImage
        );
    }

    public static CardResponseDto of(Card card, List<UpdateCommentResponseDto> comments) {
        return new CardResponseDto(
            card.getId(),
            card.getTitle(),
            card.getContents(),
            card.getDueDate(),
            comments,
            null
        );
    }
}
