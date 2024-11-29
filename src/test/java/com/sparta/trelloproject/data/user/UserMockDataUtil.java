package com.sparta.trelloproject.data.user;

import com.sparta.trelloproject.domain.auth.dto.request.UserSignInRequestDto;
import com.sparta.trelloproject.domain.auth.dto.request.UserSignUpRequestDto;
import com.sparta.trelloproject.domain.user.dto.request.UserAuthorityUpdateRequestDto;
import com.sparta.trelloproject.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.trelloproject.domain.user.entity.User;
import org.springframework.test.util.ReflectionTestUtils;

public class UserMockDataUtil {
    public static User mockUser() {
        User user = User.from("encryptPassword", mockUserSignUpRequestDto_USER());
        ReflectionTestUtils.setField(user, "id", 1L);

        return user;
    }

    public static User mockUser_DELETED() {
        User user = User.from("encryptPassword", mockUserSignUpRequestDto_USER());
        ReflectionTestUtils.setField(user, "isDeleted", true);

        return user;
    }

    public static User mockAdmin() {
        User user = User.from("encryptPassword", mockUserSignUpRequestDto_ADMIN());
        ReflectionTestUtils.setField(user, "id", 1L);

        return user;
    }

    public static UserSignUpRequestDto mockUserSignUpRequestDto_USER() {
        return new UserSignUpRequestDto("email", "password", "name", "ROLE_USER");
    }

    public static UserSignUpRequestDto mockUserSignUpRequestDto_ADMIN() {
        return new UserSignUpRequestDto("email", "password", "name", "ROLE_ADMIN");
    }

    public static UserSignUpRequestDto mockUserSignUpRequestDto_INVALID_ROLE() {
        return new UserSignUpRequestDto("email", "password", "name", "???");
    }

    public static UserSignInRequestDto mockUserSignInRequestDto() {
        return new UserSignInRequestDto("email", "password");
    }

    public static UserAuthorityUpdateRequestDto mockUserAuthorityUpdateRequestDto_INVALID_ROLE() {
        return new UserAuthorityUpdateRequestDto(1L, "???");
    }

    public static UserAuthorityUpdateRequestDto mockUserAuthorityUpdateRequestDto() {
        return new UserAuthorityUpdateRequestDto(1L, "ROLE_ADMIN");
    }

    public static UserDeleteRequestDto mockUserDeleteRequestDto_INVALID_PASSWORD() {
        return new UserDeleteRequestDto("password");
    }

    public static UserDeleteRequestDto mockUserDeleteRequestDto() {
        return new UserDeleteRequestDto("password");
    }

}
