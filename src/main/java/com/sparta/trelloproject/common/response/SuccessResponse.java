package com.sparta.trelloproject.common.response;

import com.sparta.trelloproject.common.exception.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SuccessResponse<T> extends BaseResponse {
    private T data;

    private SuccessResponse(T data) {
        super(HttpStatus.OK.value(), ResponseCode.SUCCESS.getMessage());
        this.data = data;
    }

    public static <T> SuccessResponse<T> of(T data) {
        return new SuccessResponse<>(data);
    }
}