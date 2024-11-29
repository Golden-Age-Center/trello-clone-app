package com.sparta.trelloproject.domain.board.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardDeleteRequestDto {

    @NotNull(message = "워크스페이스 ID는 비워둘 수 없습니다.")
    private Long workspaceId;

    public static BoardDeleteRequestDto of(Long workspaceId) {
        BoardDeleteRequestDto dto = new BoardDeleteRequestDto();
        dto.workspaceId = workspaceId;
        return dto;
    }



}
