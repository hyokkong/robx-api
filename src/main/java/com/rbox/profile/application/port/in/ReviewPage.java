package com.rbox.profile.application.port.in;

import java.util.List;

public record ReviewPage(List<ReviewItem> content, long total, int page, int size) {
}
