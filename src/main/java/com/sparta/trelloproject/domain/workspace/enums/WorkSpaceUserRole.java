package com.sparta.trelloproject.domain.workspace.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum WorkSpaceUserRole {
    ROLE_WORKSPACE_ADMIN(Authority.WORKSPACE_ADMIN , 1),
    ROLE_EDIT_USER(Authority.EDIT_USER , 2),
    ROLE_READ_USER(Authority.READ_USER , 3);

    private final String workspaceUserRole;
    private final Integer seq;

    public static WorkSpaceUserRole of(String role) {
        return Arrays.stream(WorkSpaceUserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 WorkspaceUserRole"));
    }

    public static class Authority {
        public static final String WORKSPACE_ADMIN = "ROLE_WORKSPACE_ADMIN";
        public static final String EDIT_USER = "ROLE_EDIT_USER";
        public static final String READ_USER = "ROLE_READ_USER";
    }
}
