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
public class BoardCreateResponseDto {

    private Long id;
    private String title;
    private String backgroundColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BoardCreateResponseDto of(Board board) {
        return new BoardCreateResponseDto(
                board.getId(),
                board.getTitle(),
                board.getBackgroundColor(),
                board.getCreatedAt(),
                board.getModifiedAt()
        );
    }
}
