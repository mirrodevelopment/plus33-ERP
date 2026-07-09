/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.dto
 * File              : AnalyticsHealthResponse.java
 * Purpose           : Data Transfer Object for request/response in Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AnalyticsHealthController
 * Related Service   : AnalyticsHealthService, AnalyticsHealthServiceImpl
 * Related Repository: AnalyticsHealthRepository
 * Related Entity    : AnalyticsHealth
 * Related DTO       : AnalyticsHealthResponse
 * Related Mapper    : AnalyticsHealthMapper
 * Related DB Table  : analytics_healths
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AnalyticsHealthController, AnalyticsHealthService, AnalyticsHealthServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Analytics Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.analytics.dto;

import java.time.LocalDateTime;

public record AnalyticsHealthResponse(
        String viewName,
        LocalDateTime lastRefreshedAt,
        Long refreshDurationMs,
        String refreshStatus
) {}
