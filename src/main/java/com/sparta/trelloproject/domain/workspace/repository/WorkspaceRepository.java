package com.sparta.trelloproject.domain.workspace.repository;

import com.sparta.trelloproject.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long>, WorkspaceQueryRepository {
    Optional<Workspace> findByIdAndUserId(Long workspaceId, Long userId);
}
