package com.sparta.trelloproject.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class DuplicateException extends ApiException {
    public DuplicateException(ResponseCode responseCode) {
        super(HttpStatus.CONFLICT, responseCode.getMessage());

        log.error("[{}] {}", HttpStatus.CONFLICT, responseCode.getMessage());
    }
}
