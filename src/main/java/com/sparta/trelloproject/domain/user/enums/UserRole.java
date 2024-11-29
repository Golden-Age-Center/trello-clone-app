package com.sparta.trelloproject.domain.user.enums;

import java.util.Arrays;

import com.sparta.trelloproject.common.exception.InvalidParameterException;
import com.sparta.trelloproject.common.exception.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    ROLE_USER(Authority.USER),
    ROLE_ADMIN(Authority.ADMIN);

    private final String userRole;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
            .filter(r -> r.name().equalsIgnoreCase(role))
            .findFirst()
            .orElseThrow(() -> new InvalidParameterException(ResponseCode.INVALID_USER_AUTHORITY));
    }

    public static class Authority {

        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}

