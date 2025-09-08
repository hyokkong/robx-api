package com.rbox.admin.policy;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record QualityPolicyRequest(@NotNull @Min(0) Integer g1Min,
                                   @NotNull @Min(0) Integer g2Min,
                                   @NotNull @Min(0) Integer g3Min,
                                   @NotNull @Min(0) Integer rptPnlA,
                                   @NotNull @Min(0) Integer rptPnlB) {}
