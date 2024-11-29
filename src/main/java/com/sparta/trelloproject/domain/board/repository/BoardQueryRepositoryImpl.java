package com.sparta.trelloproject.domain.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trelloproject.domain.board.entity.Board;
import com.sparta.trelloproject.domain.board.entity.QBoard;
import com.sparta.trelloproject.domain.card.entity.QCard;
import com.sparta.trelloproject.domain.list.entity.QLists;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
public class BoardQueryRepositoryImpl implements BoardQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Board> findAllByWorkspaceIdWithDetails(Long workspaceId) {
        QBoard board = QBoard.board;
        return queryFactory.selectFrom(board)
                .where(board.workspace.id.eq(workspaceId))
                .fetch();
    }

    @Override
    public Optional<Board> findByIdWithDetails(Long boardId) {
        QBoard board = QBoard.board;
        QLists lists = QLists.lists;
        QCard card = QCard.card;

        Board result = queryFactory.selectFrom(board)
                .distinct()
                .leftJoin(board.lists, lists).fetchJoin()
                .leftJoin(lists.cards, card).fetchJoin()
                .where(board.id.eq(boardId))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}

