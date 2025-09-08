package com.rbox.profile.application.port.in;

import java.util.Map;

public record ProfileSummary(String profLv, Map<String, String> badges, ReviewSummary review) {
}
