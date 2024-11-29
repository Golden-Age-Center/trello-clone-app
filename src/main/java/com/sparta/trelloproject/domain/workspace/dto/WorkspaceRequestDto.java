package com.sparta.trelloproject.domain.workspace.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceRequestDto {
    @NotBlank(message = "워크스페이스명은 필수 입력 값입니다.")
    private String workspaceName;
    private String workspaceDescription;
}
