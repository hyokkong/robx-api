package com.rbox.auth.adapter.in.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청")
public record LoginReq(
        @Schema(description = "사용자 이메일", example = "user@example.com") @Email @NotBlank String email,
        @Schema(description = "비밀번호", example = "Pa55w0rd!") @NotBlank @Size(min = 8, max = 128) String password
) {}
