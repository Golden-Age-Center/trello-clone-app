package com.sparta.trelloproject.domain.workspace.repository;

import com.sparta.trelloproject.domain.workspace.entity.Workspace;

import java.util.List;

public interface WorkspaceQueryRepository {
    List<Workspace> getWorkspaces(Long userId);
}
