package com.sparta.trelloproject.domain.auth.controller;

import com.sparta.trelloproject.common.response.SuccessResponse;
import com.sparta.trelloproject.domain.auth.dto.request.UserSignInRequestDto;
import com.sparta.trelloproject.domain.auth.dto.request.UserSignUpRequestDto;
import com.sparta.trelloproject.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    public final AuthService authService;

    @PostMapping("/v1/auth/sign-up")
    public ResponseEntity<SuccessResponse<Void>> signUp(
            @Valid @RequestBody UserSignUpRequestDto userSignUpRequestDto
    ) {
        String accessToken = authService.saveUser(userSignUpRequestDto);
        return ResponseEntity.ok()
                .header(AUTHORIZATION, accessToken)
                .body(SuccessResponse.of(null));
    }

    @PostMapping("/v1/auth/sign-in")
    public ResponseEntity<SuccessResponse<Void>> signIn(
            @Valid @RequestBody UserSignInRequestDto userSignInRequestDto
    ) {
        String accessToken = authService.getUserWithEmailAndPassword(userSignInRequestDto);
        return ResponseEntity.ok()
                .header(AUTHORIZATION, accessToken)
                .body(SuccessResponse.of(null));
    }
}
