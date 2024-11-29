package com.sparta.trelloproject.domain.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {

    @NotNull(message = "워크스페이스 ID는 필수입니다.")
    private Long workspaceId;

    @NotBlank(message = "제목은 비워둘 수 없습니다.")
    private String title;

    @NotNull
    private String backgroundColor;
}
