package com.sparta.trelloproject.domain.board.repository;

import com.sparta.trelloproject.domain.board.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardQueryRepository {

    List<Board> findAllByWorkspaceIdWithDetails(Long workspaceId);

    Optional<Board> findByIdWithDetails(Long boardId);
}
