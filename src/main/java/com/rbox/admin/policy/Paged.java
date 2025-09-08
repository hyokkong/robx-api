package com.rbox.admin.policy;

import java.util.List;

public record Paged<T>(List<T> content, long total, int page, int size) {}
