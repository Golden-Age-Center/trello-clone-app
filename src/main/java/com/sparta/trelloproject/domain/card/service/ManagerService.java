package com.sparta.trelloproject.domain.card.service;

import com.sparta.trelloproject.common.exception.ForbiddenException;
import com.sparta.trelloproject.common.exception.ResponseCode;
import com.sparta.trelloproject.domain.card.dto.request.ManagerRequestDto;
import com.sparta.trelloproject.domain.card.entity.Card;
import com.sparta.trelloproject.domain.card.entity.Manager;
import com.sparta.trelloproject.domain.card.repository.CardRepository;
import com.sparta.trelloproject.domain.card.repository.ManagerRepository;
import com.sparta.trelloproject.domain.user.entity.User;
import com.sparta.trelloproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ManagerService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final ManagerRepository managerRepository;

    public void addManager(ManagerRequestDto requestDto) {
        User user = userRepository.findByUserId(requestDto.getUserId());
        Card card = cardRepository.findByCardId(requestDto.getCardId());

        if (managerRepository.existsByUserIdAndCardId(user.getId(), card.getId())) {
            throw new ForbiddenException(ResponseCode.MANAGER_ALREADY_EXISTS);
        }

        Manager manager = Manager.from(user, card);
        managerRepository.save(manager);
    }
}
