package com.rbox.admin.policy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MarketPolicyRequest(@NotBlank @Size(max = 100) String polVal,
                                  @NotBlank @Pattern(regexp = "[YN]") String useYn) {}
