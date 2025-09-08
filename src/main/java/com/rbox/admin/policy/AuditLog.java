package com.rbox.admin.policy;

import java.time.Instant;
import java.util.Map;

public record AuditLog(long audId, Long uid, String actTp, String tgtTb, String tgtId,
                       Map<String, Object> diffJs, Instant regDt) {}
