package com.sparta.trelloproject.domain.user.service;

import com.sparta.trelloproject.common.exception.InvalidParameterException;
import com.sparta.trelloproject.common.exception.NotFoundException;
import com.sparta.trelloproject.domain.user.dto.request.UserAuthorityUpdateRequestDto;
import com.sparta.trelloproject.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.trelloproject.domain.user.entity.User;
import com.sparta.trelloproject.domain.user.repository.UserRepository;
import com.sparta.trelloproject.domain.workspace.service.UserWorkspaceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.sparta.trelloproject.data.user.UserMockDataUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserWorkspaceService userWorkspaceService;

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("사용자 권한 변경 테스트 케이스")
    class UpdateUserAuthorityTest {
        @Test
        @DisplayName("잘못된 사용자 권한으로 사용자 권한 수정 실패")
        public void updateUserAuthority_invalidAuthority_failure() {
            // given
            UserAuthorityUpdateRequestDto userAuthorityUpdateRequestDto = mockUserAuthorityUpdateRequestDto_INVALID_ROLE();

            User user = mockUser();

            given(userRepository.findByUserId(anyLong())).willReturn(user);

            // when, then
            assertThrows(InvalidParameterException.class, () -> userService.updateUserAuthority(userAuthorityUpdateRequestDto));
        }

        @Test
        @DisplayName("사용자 권한 수정 성공")
        public void updateUserAuthority_success() {
            // given
            UserAuthorityUpdateRequestDto userAuthorityUpdateRequestDto = mockUserAuthorityUpdateRequestDto();

            User user = mockUser();

            given(userRepository.findByUserId(anyLong())).willReturn(user);
            given(userRepository.save(user)).willReturn(user);

            // when, then
            assertDoesNotThrow(() -> userService.updateUserAuthority(userAuthorityUpdateRequestDto));
        }
    }

    @Nested
    @DisplayName("회원탈퇴 테스트 케이스")
    class DeleteUserTest {
        @Test
        @DisplayName("이미 탈퇴한 사용자라 회원탈퇴 실패")
        public void deleteUser_alreadyDeletedUser_failure() {
            // given
            long userId = 1L;
            UserDeleteRequestDto userDeleteRequestDto = mockUserDeleteRequestDto_INVALID_PASSWORD();

            User user = mockUser_DELETED();

            given(userRepository.findByUserId(anyLong())).willReturn(user);

            // when, then
            assertThrows(NotFoundException.class, () -> userService.deleteUser(userId, userDeleteRequestDto));
        }

        @Test
        @DisplayName("잘못된 비밀번호로 인해 회원탈퇴 실패")
        public void deleteUser_invalidPassword_failure() {
            // given
            long userId = 1L;
            UserDeleteRequestDto userDeleteRequestDto = mockUserDeleteRequestDto();

            User user = mockUser();

            given(userRepository.findByUserId(anyLong())).willReturn(user);
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

            // when, then
            assertThrows(InvalidParameterException.class, () -> userService.deleteUser(userId, userDeleteRequestDto));
        }

        @Test
        @DisplayName("회원탈퇴 성공")
        public void deleteUser_success() {
            // given
            long userId = 1L;
            UserDeleteRequestDto userDeleteRequestDto = mockUserDeleteRequestDto();

            User user = mockUser();

            given(userRepository.findByUserId(anyLong())).willReturn(user);
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

            // when, then
            assertDoesNotThrow(() -> userService.deleteUser(userId, userDeleteRequestDto));
            assertTrue(user.isDeleted());
        }
    }
}