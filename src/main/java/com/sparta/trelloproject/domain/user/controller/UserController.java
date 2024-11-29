package com.sparta.trelloproject.domain.user.controller;

import com.sparta.trelloproject.common.annotation.AuthUser;
import com.sparta.trelloproject.common.response.SuccessResponse;
import com.sparta.trelloproject.domain.user.dto.request.UserAuthorityUpdateRequestDto;
import com.sparta.trelloproject.domain.user.dto.request.UserDeleteRequestDto;
import com.sparta.trelloproject.domain.user.dto.response.UserAuthorityUpdateResponseDto;
import com.sparta.trelloproject.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/v1/users/authority")
    public ResponseEntity<SuccessResponse<UserAuthorityUpdateResponseDto>> changeUserAuthority(
            @Valid @RequestBody UserAuthorityUpdateRequestDto userAuthorityUpdateRequestDto
    ) {
        return ResponseEntity.ok(SuccessResponse.of(userService.updateUserAuthority(userAuthorityUpdateRequestDto)));
    }

    @DeleteMapping("/v1/users/delete")
    public ResponseEntity<SuccessResponse<Void>> deleteUser(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UserDeleteRequestDto userDeleteRequestDto
    ) {
        userService.deleteUser(authUser.getUserId(), userDeleteRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(null));
    }

}
