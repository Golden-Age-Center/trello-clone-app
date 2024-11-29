package com.sparta.trelloproject.domain.card.repository;

import static com.sparta.trelloproject.common.exception.ResponseCode.NOT_FOUND_CARD;

import com.sparta.trelloproject.common.exception.NotFoundException;
import com.sparta.trelloproject.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long>, CardQueryRepository {

    default Card findByCardId(Long cardId) {
        return findById(cardId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_CARD));
    }
}
