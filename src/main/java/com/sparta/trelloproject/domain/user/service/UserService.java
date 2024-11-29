package com.sparta.trelloproject.domain.user.service;

import com.sparta.trelloproject.common.exception.InvalidParameterException;
import com.sparta.trelloproject.common.exception.NotFoundException;
import com.sparta.trelloproject.domain.user.dto.request.UserAuthorityUpdateRequestDto;
import com.sparta.trelloproject.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.trelloproject.domain.user.dto.response.UserAuthorityUpdateResponseDto;
import com.sparta.trelloproject.domain.user.entity.User;
import com.sparta.trelloproject.domain.user.repository.UserRepository;
import com.sparta.trelloproject.domain.workspace.service.UserWorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.trelloproject.common.exception.ResponseCode.NOT_FOUND_USER;
import static com.sparta.trelloproject.common.exception.ResponseCode.WRONG_PASSWORD;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserWorkspaceService userWorkspaceService;

    public UserAuthorityUpdateResponseDto updateUserAuthority(UserAuthorityUpdateRequestDto userAuthorityUpdateRequestDto) {
        User user = userRepository.findByUserId(userAuthorityUpdateRequestDto.getUserId());
        user.updateUserAuthority(userAuthorityUpdateRequestDto);

        return UserAuthorityUpdateResponseDto.from(userRepository.save(user));
    }

    public void deleteUser(long userId, UserDeleteRequestDto userDeleteRequestDto) {
        User user = userRepository.findByUserId(userId);

        if (user.isDeleted()) {
            throw new NotFoundException(NOT_FOUND_USER);
        }

        if (!passwordEncoder.matches(userDeleteRequestDto.getPassword(), user.getPassword())) {
            throw new InvalidParameterException(WRONG_PASSWORD);
        }

        user.deleteUser();
        userWorkspaceService.deleteUserWorkspace(userId);
        userRepository.save(user);
    }

}
