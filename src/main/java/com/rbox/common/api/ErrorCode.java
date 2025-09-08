package com.rbox.common.api;

public enum ErrorCode {
    UNAUTHORIZED("ERR_UNAUTHORIZED"),
    FORBIDDEN("ERR_FORBIDDEN"),
    NOT_FOUND("ERR_NOT_FOUND"),
    INVALID_REQUEST("ERR_INVALID_REQUEST"),
    INTERNAL("ERR_INTERNAL");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
