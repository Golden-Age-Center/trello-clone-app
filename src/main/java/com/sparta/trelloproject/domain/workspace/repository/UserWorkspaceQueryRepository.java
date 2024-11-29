package com.sparta.trelloproject.domain.workspace.repository;

import com.sparta.trelloproject.domain.board.dto.BoardAuthorityDto;
import com.sparta.trelloproject.domain.card.dto.CardAuthorityDto;
import com.sparta.trelloproject.domain.list.dto.ListAuthorityDto;
import java.util.Optional;

public interface UserWorkspaceQueryRepository {
    Optional<BoardAuthorityDto> findUserWorkspaceWithUserIdAndBoardId(long workspaceId, long userId, long boardId);

    Optional<ListAuthorityDto> findUserWorkspaceWithUserIdAndListId(long workspaceId, long userId, long listId);

    Optional<CardAuthorityDto> findUserWorkspaceWithUserIdAndCardId(long workspaceId, long userId, long cardId);
}
