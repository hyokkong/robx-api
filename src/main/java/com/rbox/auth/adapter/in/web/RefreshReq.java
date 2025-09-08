package com.rbox.auth.adapter.in.web;

import jakarta.validation.constraints.NotBlank;

public record RefreshReq(@NotBlank String refreshToken) {}
