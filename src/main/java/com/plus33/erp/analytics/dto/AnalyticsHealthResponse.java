package com.plus33.erp.analytics.dto;

import java.time.LocalDateTime;

public record AnalyticsHealthResponse(
        String viewName,
        LocalDateTime lastRefreshedAt,
        Long refreshDurationMs,
        String refreshStatus
) {}
