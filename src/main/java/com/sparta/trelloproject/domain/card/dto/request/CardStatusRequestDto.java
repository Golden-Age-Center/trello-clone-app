package com.sparta.trelloproject.domain.card.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardStatusRequestDto {

    private String cardStatus;
    private Long workSpaceId;
}

