package com.sparta.trelloproject.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class InvalidParameterException extends ApiException {
    public InvalidParameterException(ResponseCode responseCode) {
        super(HttpStatus.BAD_REQUEST, responseCode.getMessage());

        log.error("[{}] {}", HttpStatus.BAD_REQUEST, responseCode.getMessage());
    }
}