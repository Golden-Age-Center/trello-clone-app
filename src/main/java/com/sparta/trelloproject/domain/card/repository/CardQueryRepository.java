package com.sparta.trelloproject.domain.card.repository;

import com.sparta.trelloproject.domain.card.dto.reponse.CardListResponseDto;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardQueryRepository {

    Page<CardListResponseDto> findCardList(String title, String contents, String manager,
        LocalDateTime dueDateFrom, LocalDateTime dueDateTo, Pageable pageable);
}
