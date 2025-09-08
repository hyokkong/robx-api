package com.rbox.common.exception;

import com.rbox.common.api.ApiError;
import com.rbox.common.api.ErrorCode;

public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;

    public ApiException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApiError toApiError() {
        return new ApiError(errorCode.code(), getMessage());
    }
}
