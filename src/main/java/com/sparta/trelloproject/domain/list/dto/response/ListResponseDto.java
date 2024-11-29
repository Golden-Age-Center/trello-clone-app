package com.sparta.trelloproject.domain.list.dto.response;

import com.sparta.trelloproject.domain.card.dto.reponse.CardDetailResponseDto;
import com.sparta.trelloproject.domain.card.dto.reponse.CardResponseDto;
import com.sparta.trelloproject.domain.list.entity.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListResponseDto {

    private Long id;
    private String title;
    private List<CardDetailResponseDto> cards;

    public static ListResponseDto from(Lists list) {
        List<CardDetailResponseDto> cardResponseDtos = list.getCards().stream()
                .map(CardDetailResponseDto::from)
                .collect(Collectors.toList());

        return new ListResponseDto(
                list.getId(),
                list.getTitle(),
                cardResponseDtos
        );
    }
}

