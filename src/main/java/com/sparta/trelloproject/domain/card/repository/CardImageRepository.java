package com.sparta.trelloproject.domain.card.repository;

import com.sparta.trelloproject.domain.card.entity.CardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardImageRepository extends JpaRepository<CardImage, Long> {

    CardImage findByCardId(Long cardId);
}
