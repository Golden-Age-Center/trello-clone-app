package com.sparta.trelloproject.domain.auth.service;

import com.sparta.trelloproject.common.config.JwtUtil;
import com.sparta.trelloproject.common.exception.DuplicateException;
import com.sparta.trelloproject.common.exception.InvalidParameterException;
import com.sparta.trelloproject.domain.auth.dto.request.UserSignInRequestDto;
import com.sparta.trelloproject.domain.auth.dto.request.UserSignUpRequestDto;
import com.sparta.trelloproject.domain.user.entity.User;
import com.sparta.trelloproject.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.sparta.trelloproject.data.user.UserMockDataUtil.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Nested
    @DisplayName("회원가입 테스트")
    class SignUpTest {
        @Test
        @DisplayName("중복 이메일로 회원가입 실패")
        public void signUp_duplicateEmail_failure() {
            // given
            UserSignUpRequestDto userSignUpRequestDto = mockUserSignUpRequestDto_USER();

            given(userRepository.existsByEmail(anyString())).willReturn(true);

            // when, then
            assertThrows(DuplicateException.class, () -> authService.saveUser(userSignUpRequestDto));
        }

        @Test
        @DisplayName("잘못된 사용자 권한으로 회원가입 실패")
        public void signUp_invalidAuthority_failure() {
            // given
            UserSignUpRequestDto userSignUpRequestDto = mockUserSignUpRequestDto_INVALID_ROLE();

            String encryptedPassword = passwordEncoder.encode("password");

            given(userRepository.existsByEmail(anyString())).willReturn(false);
            given(passwordEncoder.encode(anyString())).willReturn(encryptedPassword);

            // when, then
            assertThrows(InvalidParameterException.class, () -> authService.saveUser(userSignUpRequestDto));
        }

        @Test
        @DisplayName("사용자 회원가입 성공")
        public void signUp_success() {
            // given
            UserSignUpRequestDto userSignUpRequestDto = mockUserSignUpRequestDto_USER();

            String encryptedPassword = passwordEncoder.encode("password");
            User user = mockUser();

            given(userRepository.existsByEmail(anyString())).willReturn(false);
            given(passwordEncoder.encode(anyString())).willReturn(encryptedPassword);
            given(userRepository.save(any(User.class))).willReturn(user);

            // when, then
            assertDoesNotThrow(() -> authService.saveUser(userSignUpRequestDto));
        }
    }

    @Nested
    @DisplayName("로그인 테스트")
    class SignInTest {
        @Test
        @DisplayName("잘못된 비밀번호로 인해 로그인 실패")
        public void signIn_invalidPassword_failure() {
            // given
            UserSignInRequestDto userSignInRequestDto = mockUserSignInRequestDto();

            User user = mockUser();

            given(userRepository.findByUserEmail(anyString())).willReturn(user);
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

            // when, then
            assertThrows(InvalidParameterException.class, () -> authService.getUserWithEmailAndPassword(userSignInRequestDto));
        }

        @Test
        @DisplayName("로그인 성공")
        public void signIn_success() {
            // given
            UserSignInRequestDto userSignInRequestDto = mockUserSignInRequestDto();

            User user = mockUser();

            given(userRepository.findByUserEmail(anyString())).willReturn(user);
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

            // when, then
            assertDoesNotThrow(() -> authService.getUserWithEmailAndPassword(userSignInRequestDto));
        }
    }
}