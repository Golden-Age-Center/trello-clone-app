package com.sparta.trelloproject.domain.card.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestDto {

    private Long listId;
    private String title;
    private String contents;
    private LocalDateTime dueDate;
    private Long workSpaceId;
    private String cardStatus;
}

