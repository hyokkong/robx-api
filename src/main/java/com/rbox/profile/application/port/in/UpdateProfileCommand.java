package com.rbox.profile.application.port.in;

import java.util.Map;

public record UpdateProfileCommand(String bio, String linkUrl, Map<String, String> badges) {
}
