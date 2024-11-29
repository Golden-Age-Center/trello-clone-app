package com.sparta.trelloproject.domain.auth.service;

import com.sparta.trelloproject.common.config.JwtUtil;
import com.sparta.trelloproject.common.exception.DuplicateException;
import com.sparta.trelloproject.common.exception.InvalidParameterException;
import com.sparta.trelloproject.domain.auth.dto.request.UserSignInRequestDto;
import com.sparta.trelloproject.domain.auth.dto.request.UserSignUpRequestDto;
import com.sparta.trelloproject.domain.user.entity.User;
import com.sparta.trelloproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.trelloproject.common.exception.ResponseCode.DUPLICATE_EMAIL;
import static com.sparta.trelloproject.common.exception.ResponseCode.WRONG_EMAIL_OR_PASSWORD;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public String saveUser(UserSignUpRequestDto userSignUpRequestDto) {
        boolean isExistUser = userRepository.existsByEmail(userSignUpRequestDto.getEmail());

        if (isExistUser) {
            throw new DuplicateException(DUPLICATE_EMAIL);
        }

        String encryptPassword = passwordEncoder.encode(userSignUpRequestDto.getPassword());
        User user = User.from(encryptPassword, userSignUpRequestDto);

        User savedUser = userRepository.save(user);

        return jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), savedUser.getAuthority());
    }

    @Transactional(readOnly = true)
    public String getUserWithEmailAndPassword(UserSignInRequestDto userSignInRequestDto) {
        User user = userRepository.findByUserEmail(userSignInRequestDto.getEmail());

        if (!passwordEncoder.matches(userSignInRequestDto.getPassword(), user.getPassword())) {
            throw new InvalidParameterException(WRONG_EMAIL_OR_PASSWORD);
        }

        return jwtUtil.createToken(user.getId(), user.getEmail(), user.getAuthority());
    }
}
