package com.rbox.auth.adapter.in.web;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 갱신 요청")
public record RefreshReq(
        @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9") @NotBlank String refreshToken
) {}
