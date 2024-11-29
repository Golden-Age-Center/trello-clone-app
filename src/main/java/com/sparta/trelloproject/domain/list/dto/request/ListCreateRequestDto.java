package com.sparta.trelloproject.domain.list.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListCreateRequestDto {
    @NotBlank(message = "리스트 제목은 필수입니다.")
    private String title;

    @NotNull(message = "보드 고유번호는 필수입니다.")
    private Long boardId;

    @NotNull(message = "워크스페이스 고유번호는 필수입니다.")
    private Long workspaceId;

}
