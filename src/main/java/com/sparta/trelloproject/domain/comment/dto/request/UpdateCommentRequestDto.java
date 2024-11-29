package com.sparta.trelloproject.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentRequestDto {
    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String contents;
}
