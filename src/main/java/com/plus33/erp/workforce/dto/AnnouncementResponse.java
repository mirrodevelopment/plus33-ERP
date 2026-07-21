package com.plus33.erp.workforce.dto;

import java.util.Map;

public record AnnouncementResponse(
    Long id,
    String title,
    String content,
    String priority,
    String publisher,
    String publisherRole,
    String publisherColor,
    String mediaType,
    String date,
    String createdAt,
    String expiresAt,
    String deletedAt,
    boolean isDeleted,
    boolean read,
    Map<String, Integer> reactions,
    String imageUrl,
    Long targetRegionId,
    Long targetStoreId
) {}
