package com.sparta.trelloproject.domain.workspace.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WorkspaceResponseDto {
    private Long workspaceId;
    private String workspaceName;
    private String workspaceDescription;
}
