package com.sparta.trelloproject.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class NotFoundException extends ApiException {
    public NotFoundException(ResponseCode responseCode) {
        super(HttpStatus.NOT_FOUND, responseCode.getMessage());

        log.error("[{}] {}", HttpStatus.NOT_FOUND, responseCode.getMessage());
    }
}
