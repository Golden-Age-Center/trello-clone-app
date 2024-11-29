package com.sparta.trelloproject.domain.list.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.sparta.trelloproject.domain.list.entity.QLists.lists;

@Repository
@RequiredArgsConstructor
public class ListQueryRepositoryImpl implements ListQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void updateSequence(long listId, int sequence) {
        jpaQueryFactory.update(lists)
                .set(lists.sequence, sequence)
                .where(lists.id.eq(listId))
                .execute();
    }

    @Override
    public void updateSequenceIncrement(Integer sequence, Integer originSequence, long boardId) {
        jpaQueryFactory.update(lists)
                .set(lists.sequence, lists.sequence.add(1))
                .where(greaterThanOrEqualToSequence(sequence), lessThanSequence(originSequence), lists.board.id.eq(boardId))
                .execute();
    }

    @Override
    public void updateSequenceDecrement(Integer sequence, Integer originSequence, long boardId) {
        jpaQueryFactory.update(lists)
                .set(lists.sequence, lists.sequence.add(-1))
                .where(lessThanOrEqualToSequence(sequence), greaterThanSequence(originSequence), lists.board.id.eq(boardId))
                .execute();
    }

    private BooleanExpression lessThanOrEqualToSequence(Integer sequence) {
        if(sequence == null) return null;

        return lists.sequence.loe(sequence);
    }

    private BooleanExpression lessThanSequence(Integer sequence) {
        if(sequence == null) return null;

        return lists.sequence.lt(sequence);
    }

    private BooleanExpression greaterThanOrEqualToSequence(Integer sequence) {
        if(sequence == null) return null;

        return lists.sequence.goe(sequence);
    }

    private BooleanExpression greaterThanSequence(Integer sequence) {
        if(sequence == null) return null;

        return lists.sequence.gt(sequence);
    }
}
