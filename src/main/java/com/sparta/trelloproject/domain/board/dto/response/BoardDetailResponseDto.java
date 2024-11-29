package com.sparta.trelloproject.domain.board.dto.response;

import com.sparta.trelloproject.domain.board.entity.Board;
import com.sparta.trelloproject.domain.list.dto.response.ListResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetailResponseDto {

    private Long id;
    private String title;
    private String backgroundColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ListResponseDto> lists;

    public static BoardDetailResponseDto from(Board board, List<ListResponseDto> lists) {
        return new BoardDetailResponseDto(
                board.getId(),
                board.getTitle(),
                board.getBackgroundColor(),
                board.getCreatedAt(),
                board.getModifiedAt(),
                lists
        );
    }
}
