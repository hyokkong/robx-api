package com.rbox.admin.policy;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BreedingPolicyRequest(@NotNull @Min(0) Integer etaMin,
                                    @NotNull @Min(0) Integer etaMax,
                                    @NotNull @Min(0) Integer cdlDay,
                                    @NotNull @Min(0) Integer layGap) {}
