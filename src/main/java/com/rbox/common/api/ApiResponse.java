package com.rbox.common.api;

public record ApiResponse<T>(T data, Object meta, ApiError error) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, null, null);
    }

    public static <T> ApiResponse<T> error(ApiError error) {
        return new ApiResponse<>(null, null, error);
    }
}
