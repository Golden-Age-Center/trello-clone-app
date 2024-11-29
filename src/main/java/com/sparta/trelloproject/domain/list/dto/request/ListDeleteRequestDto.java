package com.sparta.trelloproject.domain.list.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListDeleteRequestDto {
    @NotNull(message = "워크스페이스 고유번호는 필수입니다.")
    private Long workspaceId;
}
