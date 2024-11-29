package com.sparta.trelloproject.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaveCommentRequestDto {
    @NotNull
    private Long workspaceId;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String contents;
}
