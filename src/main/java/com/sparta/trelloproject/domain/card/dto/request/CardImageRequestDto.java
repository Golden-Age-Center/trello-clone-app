package com.sparta.trelloproject.domain.card.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardImageRequestDto {

    private String path;
    private String fileName;
    private String originName;
    private String extension;

}

