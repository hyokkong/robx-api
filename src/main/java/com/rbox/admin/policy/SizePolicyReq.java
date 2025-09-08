package com.rbox.admin.policy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SizePolicyReq(Integer ageMin, Integer ageMax,
                                Double wtMin, Double wtMax,
                                @NotBlank @Pattern(regexp = "AGE|WT|BOTH") String adjTp) {}
