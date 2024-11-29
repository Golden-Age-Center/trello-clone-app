package com.sparta.trelloproject.domain.workspace.repository;

import com.sparta.trelloproject.common.exception.NotFoundException;
import com.sparta.trelloproject.common.exception.ResponseCode;
import com.sparta.trelloproject.domain.board.dto.BoardAuthorityDto;
import com.sparta.trelloproject.domain.card.dto.CardAuthorityDto;
import com.sparta.trelloproject.domain.card.entity.Card;
import com.sparta.trelloproject.domain.list.dto.ListAuthorityDto;
import com.sparta.trelloproject.domain.workspace.entity.UserWorkspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWorkSpaceRepository extends JpaRepository<UserWorkspace, Long>, UserWorkspaceQueryRepository {
    UserWorkspace findByWorkspaceIdAndUserId(Long workspaceId, Long userId);
    void deleteByUserId(long userId);

    default BoardAuthorityDto findByBoardId(long workspaceId, long userId, long boardId) {
        return findUserWorkspaceWithUserIdAndBoardId(workspaceId, userId, boardId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_BOARD));
    }

    default ListAuthorityDto findByListId(long workspaceId, long userId, long listId) {
        return findUserWorkspaceWithUserIdAndListId(workspaceId, userId, listId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_LIST));
    }

    default CardAuthorityDto findByCardId(long workspaceId, long userId, long cardId){
        return findUserWorkspaceWithUserIdAndCardId(workspaceId, userId, cardId)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_CARD));
    }
}
