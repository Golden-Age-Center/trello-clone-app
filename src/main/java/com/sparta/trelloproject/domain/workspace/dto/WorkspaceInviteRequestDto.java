package com.sparta.trelloproject.domain.workspace.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WorkspaceInviteRequestDto {
    @Email
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    String email;
    @NotNull(message = "워크스페이스 ID는 필수 입력 값입니다.")
    Long workspaceId;
    @NotBlank(message = "워크스페이스 역할은 필수 입력 값입니다.")
    String workspaceUserRole;
}
