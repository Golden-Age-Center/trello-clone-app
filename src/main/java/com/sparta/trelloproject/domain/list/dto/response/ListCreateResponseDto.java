package com.sparta.trelloproject.domain.list.dto.response;

import com.sparta.trelloproject.domain.list.entity.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListCreateResponseDto {
    private long listId;
    private String listTitle;
    private int listSequence;

    public static ListCreateResponseDto from(Lists list) {
        return new ListCreateResponseDto(
                list.getId(),
                list.getTitle(),
                list.getSequence()
        );
    }
}