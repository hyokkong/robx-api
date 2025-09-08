package com.rbox.profile.application.port.in;

import java.util.Map;

public record ProfilePublic(Long userId, String nick, String profLv,
        Map<String, String> badges, String bio, String linkUrl, ReviewSummary review) {
}
