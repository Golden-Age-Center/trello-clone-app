package com.sparta.trelloproject.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class UnauthorizedException extends ApiException {
    public UnauthorizedException(ResponseCode responseCode) {
        super(HttpStatus.UNAUTHORIZED, responseCode.getMessage());

        log.error("[{}] {}", HttpStatus.UNAUTHORIZED, responseCode.getMessage());
    }
}
