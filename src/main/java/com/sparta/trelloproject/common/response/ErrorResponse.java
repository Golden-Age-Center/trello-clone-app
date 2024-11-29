package com.sparta.trelloproject.common.response;

import com.sparta.trelloproject.common.exception.ApiException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse extends BaseResponse {
    LocalDateTime timestamp;

    private ErrorResponse(ApiException apiException) {
        super(apiException.getHttpStatus().value(), apiException.getMessage());
        this.timestamp = LocalDateTime.now();
    }

    private ErrorResponse(int code, String message) {
        super(code, message);
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse from(ApiException apiException) {
        return new ErrorResponse(apiException);
    }

    public static ErrorResponse of(int code, String message) {
        return new ErrorResponse(code, message);
    }
}