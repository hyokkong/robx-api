package com.rbox.admin.policy;

import java.time.Instant;

public record MarketPolicy(String polCd, String polVal, String useYn, Instant updDt) {}
