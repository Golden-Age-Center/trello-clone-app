package com.sparta.trelloproject.domain.workspace.repository;

import static com.sparta.trelloproject.domain.board.entity.QBoard.board;
import static com.sparta.trelloproject.domain.card.entity.QCard.card;
import static com.sparta.trelloproject.domain.list.entity.QLists.lists;
import static com.sparta.trelloproject.domain.workspace.entity.QUserWorkspace.userWorkspace;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trelloproject.domain.board.dto.BoardAuthorityDto;
import com.sparta.trelloproject.domain.card.dto.CardAuthorityDto;
import com.sparta.trelloproject.domain.list.dto.ListAuthorityDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserWorkspaceQueryRepositoryImpl implements UserWorkspaceQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<BoardAuthorityDto> findUserWorkspaceWithUserIdAndBoardId(long workspaceId, long userId, long boardId) {
        return Optional.ofNullable(
                jpaQueryFactory.select(
                                Projections.fields(BoardAuthorityDto.class,
                                        board.id.as("boardId"),
                                        board.title.as("title"),
                                        board.backgroundColor.as("backgroundColor"),
                                        board.lists.size().as("listCount"),
                                        board.workspace.id.as("workspaceId"),
                                        userWorkspace.workSpaceUserRole.as("workspaceAuthority")
                                )
                        )
                        .distinct()
                        .from(userWorkspace)
                        .join(board)
                        .on(userWorkspace.workspace.id.eq(board.workspace.id))
                        .where(
                                userWorkspace.workspace.id.eq(workspaceId),
                                userWorkspace.user.id.eq(userId),
                                board.id.eq(boardId)
                        )
                        .fetchFirst()
        );
    }

    @Override
    public Optional<ListAuthorityDto> findUserWorkspaceWithUserIdAndListId(long workspaceId, long userId, long listId) {
        return Optional.ofNullable(
                jpaQueryFactory.select(
                                Projections.fields(ListAuthorityDto.class,
                                        lists.id.as("listId"),
                                        userWorkspace.workSpaceUserRole.as("workspaceAuthority"),
                                        lists.board.id.as("boardId"),
                                        lists.sequence.as("sequence"),
                                        lists.title.as("title"),
                                        ExpressionUtils.as(
                                                JPAExpressions.select(Wildcard.count)
                                                        .from(lists)
                                                        .where(lists.board.id.eq(board.id))
                                                        .groupBy(lists.board.id),
                                                "listCount"
                                        )
                                )
                        )
                        .distinct()
                        .from(userWorkspace)
                        .join(board)
                        .on(userWorkspace.workspace.id.eq(board.workspace.id))
                        .join(lists)
                        .on(board.id.eq(lists.board.id))
                        .where(
                                userWorkspace.workspace.id.eq(workspaceId),
                                userWorkspace.user.id.eq(userId),
                                lists.id.eq(listId)
                        )
                        .fetchFirst()
        );
    }

    @Override
    public Optional<CardAuthorityDto> findUserWorkspaceWithUserIdAndCardId(long workspaceId, long userId, long cardId) {
        return Optional.ofNullable(
            jpaQueryFactory.select(
                    Projections.fields(CardAuthorityDto.class,
                        card.id.as("cardId"),
                        userWorkspace.workSpaceUserRole.as("workspaceAuthority"),
                        card.list.id.as("listId"),
                        card.title.as("title"),
                        card.contents.as("contents")
                    )
                )
                .distinct()
                .from(userWorkspace)
                .join(board)
                .on(userWorkspace.workspace.id.eq(board.workspace.id))
                .join(lists)
                .on(board.id.eq(lists.board.id))
                .join(card)
                .on(lists.id.eq(card.list.id))
                .where(
                    userWorkspace.workspace.id.eq(workspaceId),
                    userWorkspace.user.id.eq(userId),
                    card.id.eq(cardId)
                )
                .fetchFirst()
        );
    }
}
