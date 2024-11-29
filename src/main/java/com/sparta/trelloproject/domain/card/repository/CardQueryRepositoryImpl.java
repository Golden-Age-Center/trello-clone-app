package com.sparta.trelloproject.domain.card.repository;

import static com.sparta.trelloproject.domain.card.entity.QCard.card;
import static com.sparta.trelloproject.domain.card.entity.QManager.manager;
import static com.sparta.trelloproject.domain.user.entity.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trelloproject.domain.card.dto.reponse.CardListResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardQueryRepositoryImpl implements CardQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CardListResponseDto> findCardList(String title, String contents, String managerName,
        LocalDateTime dueDateFrom, LocalDateTime dueDateTo, Pageable pageable) {
        List<CardListResponseDto> results = queryFactory
            .select(
                Projections.constructor(
                    CardListResponseDto.class,
                    card.id,
                    card.title,
                    card.contents,
                    card.dueDate
                )
            )
            .from(card)
            .leftJoin(manager).on(manager.card.eq(card))
            .leftJoin(manager.user, user)
            .where(
                titleContains(title),
                contentsContains(contents),
                managerNicknameContains(managerName),
                dueDateBetween(dueDateFrom, dueDateTo)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(card.id.desc())
            .fetch();

        Long totalCount = queryFactory
            .select(Wildcard.count)
            .from(card)
            .leftJoin(manager).on(manager.card.eq(card))
            .leftJoin(manager.user, user)
            .where(
                titleContains(title),
                contentsContains(contents),
                managerNicknameContains(managerName),
                dueDateBetween(dueDateFrom, dueDateTo)
            ).fetchOne();

        return new PageImpl<>(results, pageable, totalCount);
    }

    private BooleanExpression titleContains(String title) {
        return title != null ? card.title.startsWith(title) : null;
    }

    private BooleanExpression contentsContains(String contents) {
        return contents != null ? card.contents.startsWith(contents) : null;
    }

    private BooleanExpression managerNicknameContains(String managerName) {
        return managerName != null ? user.name.startsWith(managerName) : null;
    }

    private BooleanExpression dueDateBetween(LocalDateTime dueDateFrom, LocalDateTime dueDateTo) {
        if (dueDateFrom != null && dueDateTo != null) {
            return card.dueDate.between(dueDateFrom, dueDateTo);
        } else if (dueDateFrom != null) {
            return card.dueDate.after(dueDateFrom);
        } else if (dueDateTo != null) {
            return card.dueDate.before(dueDateTo);
        } else {
            return null;
        }
    }
}
