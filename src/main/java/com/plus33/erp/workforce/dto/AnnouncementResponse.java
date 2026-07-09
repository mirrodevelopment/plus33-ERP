package com.plus33.erp.workforce.dto;

import java.util.Map;

public record AnnouncementResponse(
    Long id,
    String title,
    String content,
    String priority,
    String publisher,
    String date,
    String createdAt,
    boolean read,
    Map<String, Integer> reactions
) {}
